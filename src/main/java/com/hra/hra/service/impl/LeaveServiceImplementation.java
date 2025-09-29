package com.hra.hra.service.impl;

import com.hra.hra.config.FiscalYearUtil;
import com.hra.hra.config.LeavePolicyProperties;
import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import com.hra.hra.dto.*;
import com.hra.hra.entity.*;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LeaveServiceImplementation {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private  LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private  LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private  LeaveRequestSegmentRepository segmentRepository;

    @Autowired
    private  LeaveTransactionRepository transactionRepository;

    @Autowired
    private  LeavePolicyProperties policy;

    @Autowired
    private Response response;

    @Autowired
    private ModelMapper mapper;

    private static final int SCALE = 2;

    /* -------------------------
       Helper rounding / math
       ------------------------*/
    private BigDecimal scale(BigDecimal v) {
        return v.setScale(SCALE, RoundingMode.HALF_UP);
    }

    // inclusive days between
    private BigDecimal daysInclusive(LocalDate a, LocalDate b) {
        long days = ChronoUnit.DAYS.between(a, b) + 1;
        return BigDecimal.valueOf(days);
    }

    /* -------------------------
       Split date range into month segments
       ------------------------*/
    private static class SegmentInfo {
        LocalDate start;
        LocalDate end;
        YearMonth ym;
        SegmentInfo(LocalDate s, LocalDate e) { this.start = s; this.end = e; this.ym = YearMonth.from(s); }
    }

    private List<SegmentInfo> splitByMonth(LocalDate start, LocalDate end) {
        List<SegmentInfo> out = new ArrayList<>();
        YearMonth cur = YearMonth.from(start);
        YearMonth last = YearMonth.from(end);
        while (!cur.isAfter(last)) {
            LocalDate segStart = cur.equals(YearMonth.from(start)) ? start : cur.atDay(1);
            LocalDate segEnd = cur.equals(YearMonth.from(end)) ? end : cur.atEndOfMonth();
            out.add(new SegmentInfo(segStart, segEnd));
            cur = cur.plusMonths(1);
        }
        return out;
    }

    /* -------------------------
       Onboarding: give prorated accrual for join month
       Call this when creating employee
       ------------------------*/
    @Transactional
    public void initializeBalancesForJoin(Employee employee) {
        LocalDate join = employee.getJoinDate();
        YearMonth joinYm = YearMonth.from(join);

        for (LeaveType lt : LeaveType.values()) {
            BigDecimal fullMonthly = policy.getMonthlyAccrualForType(lt);
            int length = join.lengthOfMonth();
            int daysRemaining = join.lengthOfMonth() - join.getDayOfMonth() + 1;
            BigDecimal prorata = fullMonthly.multiply(BigDecimal.valueOf(daysRemaining))
                    .divide(BigDecimal.valueOf(length), 6, RoundingMode.HALF_UP);
            prorata = scale(prorata);

            String fy = FiscalYearUtil.fiscalYearFor(join);
            LeaveBalance balance = this.leaveBalanceRepository
                    .findByEmployeeIdAndFiscalYearAndLeaveType(employee.getId(), fy, lt)
                    .orElse(LeaveBalance.builder()
                            .employee(employee)
                            .fiscalYear(fy)
                            .leaveType(lt)
                            .balance(BigDecimal.ZERO)
                            .build());

            balance.setBalance(scale(balance.getBalance().add(prorata)));
            balance.setLastUpdated(LocalDateTime.now());
            this.leaveBalanceRepository.save(balance);

            // transaction
            LeaveTransaction tx = LeaveTransaction.builder()
                    .employee(employee)
                    .fiscalYear(fy)
                    .leaveType(lt)
                    .amount(prorata)
                    .transactionType("ACCRUAL")
                    .yearMonth(joinYm.toString())
                    .createdAt(LocalDateTime.now())
                    .note("Prorated onboarding accrual")
                    .build();
            this.transactionRepository.save(tx);
        }
    }

    /* -------------------------
       Monthly accrual (idempotent):
       Run on 1st of month (or invoked manually). Adds monthly accrual for employees who
       are active and haven't already received accrual for this year-Month.
       ------------------------*/
    @Transactional
    public void runMonthlyAccrual(LocalDate forDate) {
        YearMonth ym = YearMonth.from(forDate);
        String ymStr = ym.toString();
        String fy = FiscalYearUtil.fiscalYearFor(forDate);

        List<Employee> employees = this.employeeRepository.findAll();

        for (Employee emp : employees) {
            if (!emp.isActive()) continue;
            if (emp.getJoinDate().isAfter(ym.atEndOfMonth())) continue;

            for (LeaveType lt : LeaveType.values()) {
                // idempotent check including leaveType
                boolean exists = this.transactionRepository
                        .existsByEmployeeIdAndTransactionTypeAndYearMonthAndLeaveType(
                                emp.getId(), "ACCRUAL", ymStr, lt);
                if (exists) continue;

                BigDecimal monthly = policy.getMonthlyAccrualForType(lt); // e.g. 7/12 or defined constant

                // If joined in same month but after day 1, onboarding gave prorata - skip full accrual
                if (YearMonth.from(emp.getJoinDate()).equals(ym) && emp.getJoinDate().getDayOfMonth() > 1) {
                    continue;
                }

                LeaveBalance balance = this.leaveBalanceRepository
                        .findByEmployeeIdAndFiscalYearAndLeaveType(emp.getId(), fy, lt)
                        .orElse(LeaveBalance.builder()
                                .employee(emp)
                                .fiscalYear(fy)
                                .leaveType(lt)
                                .balance(BigDecimal.ZERO)
                                .build());

                BigDecimal toAdd = scale(monthly);

                // March rounding reconciliation (optional):
                if (ym.getMonth() == Month.MARCH) {
                    // compute total already credited this FY for this leave type
                    BigDecimal creditedSoFar = transactionRepository.sumAccrualsByEmployeeAndFiscalYearAndLeaveType(emp.getId(), fy, lt);
                    // expectedTotal = annual allocation (e.g. 7)
                    BigDecimal expectedTotal = policy.getAnnualAllocationForType(lt);
                    BigDecimal remainder = expectedTotal.subtract(creditedSoFar.add(toAdd));
                    if (remainder.compareTo(BigDecimal.ZERO) > 0) {
                        toAdd = toAdd.add(remainder); // add leftover to March
                    }
                }

                balance.setBalance(scale(balance.getBalance().add(toAdd)));
                balance.setLastUpdated(LocalDateTime.now());
                this.leaveBalanceRepository.save(balance);

                LeaveTransaction tx = LeaveTransaction.builder()
                        .employee(emp)
                        .fiscalYear(fy)
                        .leaveType(lt)
                        .amount(scale(toAdd))
                        .transactionType("ACCRUAL")
                        .yearMonth(ymStr)
                        .createdAt(LocalDateTime.now())
                        .note("Monthly accrual")
                        .build();
                transactionRepository.save(tx);
            }
        }
    }


    /* -------------------------
       Request leave (PENDING). We compute expectedPaid/unpaid based on current approved usage,
       but do not affect balances until approval.
       ------------------------*/
    @Transactional
    public Response requestLeave(LeaveRequestDto dto) {
        Employee emp = this.employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NoDataExist("Employee not found"));

        if (dto.getEndDate().isBefore(dto.getStartDate()))
            throw new IllegalArgumentException("endDate before startDate");

        BigDecimal totalDays = daysInclusive(dto.getStartDate(), dto.getEndDate()).setScale(SCALE, RoundingMode.HALF_UP);

        // compute expectation per month using current approved segments
        List<SegmentInfo> segments = splitByMonth(dto.getStartDate(), dto.getEndDate());
        BigDecimal expectedPaid = BigDecimal.ZERO;
        BigDecimal expectedUnpaid = BigDecimal.ZERO;

        for (SegmentInfo s : segments) {
            BigDecimal segDays = daysInclusive(s.start, s.end);
            String ym = s.ym.toString();
            BigDecimal usedPaid = segmentRepository.sumPaidDaysByEmployeeAndYearMonth(emp.getId(), ym);
            BigDecimal remainingCap = policy.getMonthlyPaidCap().subtract(scale(usedPaid));
            if (remainingCap.compareTo(BigDecimal.ZERO) < 0) remainingCap = BigDecimal.ZERO;

            BigDecimal paidPossible = segDays.min(remainingCap);

            // available balance for this leave type in that fiscal year:
            String fy = FiscalYearUtil.fiscalYearFor(s.start);
            BigDecimal availableBal = leaveBalanceRepository
                    .findByEmployeeIdAndFiscalYearAndLeaveType(emp.getId(), fy, dto.getLeaveType())
                    .map(LeaveBalance::getBalance)
                    .orElse(BigDecimal.ZERO);

            BigDecimal paidFromBalance = paidPossible.min(availableBal);
            BigDecimal unpaid = segDays.subtract(paidFromBalance);

            expectedPaid = expectedPaid.add(paidFromBalance);
            expectedUnpaid = expectedUnpaid.add(unpaid);
        }

        LeaveRequest saved = LeaveRequest.builder()
                .employee(emp)
                .leaveType(dto.getLeaveType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .requestedDays(totalDays)
                .expectedPaidDays(scale(expectedPaid))
                .expectedUnpaidDays(scale(expectedUnpaid))
                .status(LeaveStatus.PENDING)
                .reason(dto.getReason())
                .createdAt(LocalDateTime.now())
                .build();
        leaveRequestRepository.save(saved);

        LeaveRequestResponseDto result =  LeaveRequestResponseDto.builder()
                .id(saved.getId())
                .employeeId(emp.getId())
                .leaveType(saved.getLeaveType())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .requestedDays(saved.getRequestedDays())
                .expectedPaidDays(saved.getExpectedPaidDays())
                .expectedUnpaidDays(saved.getExpectedUnpaidDays())
                .status(saved.getStatus())
                .reason(saved.getReason())
                .build();

        response.setStatus("SUCCESS");
        response.setMessage("Leave Applied successfully");
        response.setData(result);
        response.setStatusCode(201);
        response.setResponse_message("Process Executed success");

        return response;
    }

    /* -------------------------
       Approve leave: finalizes splits, updates balances and creates transactions & segments
       ------------------------*/
    @Transactional
    public Response approveLeave(Long requestId, Long approverId) {
        LeaveRequest req = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoDataExist("Leave request not found"));

        if (req.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be approved");
        }

        List<SegmentInfo> segments = splitByMonth(req.getStartDate(), req.getEndDate());

        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalUnpaid = BigDecimal.ZERO;
        List<LeaveRequestSegment> toSaveSegments = new ArrayList<>();
        List<LeaveTransaction> transactions = new ArrayList<>();

        Long empId = req.getEmployee().getId();

        for (SegmentInfo s : segments) {
            BigDecimal segDays = daysInclusive(s.start, s.end);
            String ym = s.ym.toString();
            String fy = FiscalYearUtil.fiscalYearFor(s.start);

            // Lock total leave balance row for this employee and fiscal year
            List<LeaveBalance> balances = this.leaveBalanceRepository.findByEmployeeIdAndFiscalYearForUpdateList(empId, fy);
            LeaveBalance balance;
            if (balances.isEmpty()) {
                balance = LeaveBalance.builder()
                        .employee(req.getEmployee())
                        .fiscalYear(fy)
                        .balance(BigDecimal.ZERO)
                        .build();
            } else {
                balance = balances.get(0); // take first row if duplicates exist
            }

            BigDecimal availableBalance = balance.getBalance();

            // Deduct from total balance (regardless of leave type)
            BigDecimal paidFromBalance = segDays.min(availableBalance);
            paidFromBalance = scale(paidFromBalance);

            BigDecimal unpaid = segDays.subtract(paidFromBalance);
            unpaid = scale(unpaid.max(BigDecimal.ZERO));

            // Update total balance
            if (paidFromBalance.compareTo(BigDecimal.ZERO) > 0) {
                balance.setBalance(scale(balance.getBalance().subtract(paidFromBalance)));
                balance.setLastUpdated(LocalDateTime.now());
                leaveBalanceRepository.save(balance);

                LeaveTransaction deduction = LeaveTransaction.builder()
                        .employee(req.getEmployee())
                        .fiscalYear(fy)
                        .leaveType(req.getLeaveType()) // keep type for reporting
                        .amount(paidFromBalance.negate())
                        .transactionType("DEDUCTION")
                        .yearMonth(ym)
                        .createdAt(LocalDateTime.now())
                        .note("Approved leave deduction (total balance)")
                        .build();
                transactions.add(deduction);
            }

            if (unpaid.compareTo(BigDecimal.ZERO) > 0) {
                LeaveTransaction unpaidTx = LeaveTransaction.builder()
                        .employee(req.getEmployee())
                        .fiscalYear(fy)
                        .leaveType(req.getLeaveType())
                        .amount(unpaid.negate())
                        .transactionType("UNPAID_MARK")
                        .yearMonth(ym)
                        .createdAt(LocalDateTime.now())
                        .note("Approved unpaid leave - payroll deduction")
                        .build();
                transactions.add(unpaidTx);
            }

            // Create segment record for reporting
            LeaveRequestSegment seg = LeaveRequestSegment.builder()
                    .leaveRequest(req)
                    .yearMonth(ym)
                    .segmentStart(s.start)
                    .segmentEnd(s.end)
                    .days(scale(segDays))
                    .paidDays(paidFromBalance)
                    .unpaidDays(unpaid)
                    .build();
            toSaveSegments.add(seg);

            totalPaid = totalPaid.add(paidFromBalance);
            totalUnpaid = totalUnpaid.add(unpaid);
        }

        // Persist transactions & segments
        transactionRepository.saveAll(transactions);
        segmentRepository.saveAll(toSaveSegments);

        req.setPaidDays(scale(totalPaid));
        req.setUnpaidDays(scale(totalUnpaid));
        req.setStatus(LeaveStatus.APPROVED);
        req.setApprovedAt(LocalDateTime.now());
        req.setApprovedBy(approverId);
        leaveRequestRepository.save(req);

        LeaveRequestResponseDto result = LeaveRequestResponseDto.builder()
                .id(req.getId())
                .employeeId(empId)
                .leaveType(req.getLeaveType())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .requestedDays(req.getRequestedDays())
                .expectedPaidDays(req.getExpectedPaidDays())
                .expectedUnpaidDays(req.getExpectedUnpaidDays())
                .status(req.getStatus())
                .build();

        response.setStatus("SUCCESS");
        response.setMessage("Leave Approved successfully");
        response.setData(result);
        response.setStatusCode(200);
        response.setResponse_message("Process Executed success");

        return response;
    }

    // Calculate monthly allocated leave to get leave balances
    private int getMonthsElapsedInFiscalYear(String fiscalYear, LocalDate joinDate) {
        // Fiscal year starts in April
        int fiscalStartYear = Integer.parseInt(fiscalYear.substring(0, 4));
        LocalDate fiscalStart = LocalDate.of(fiscalStartYear, Month.APRIL, 1);

        // Start counting from later of fiscal start or join date
        LocalDate accrualStart = joinDate.isAfter(fiscalStart) ? joinDate : fiscalStart;

        LocalDate today = LocalDate.now();
        if (today.isBefore(accrualStart)) {
            return 0;
        }

        Period p = Period.between(accrualStart.withDayOfMonth(1), today.withDayOfMonth(1));
        return p.getYears() * 12 + p.getMonths() + 1; // inclusive
    }


    // Get balances for an employee (cumulative monthly accrual with carry forward)
    public Response getBalances(Long employeeId, String fiscalYear) {
        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(()-> new NoDataExist("No employee fetched with given employee ID"));

        List<LeaveBalanceDto> balances = Arrays.stream(LeaveType.values())
                .map(type -> {
                    // 1. Months passed till now in this FY
                    int monthsElapsed = getMonthsElapsedInFiscalYear(fiscalYear, employee.getJoinDate());
                    // 2. Total accrual till now (monthly * months elapsed)
                    BigDecimal accrued = policy
                            .getMonthlyAccrualForType(type)
                            .multiply(BigDecimal.valueOf(monthsElapsed));

                    // 3. Leaves already used till now
                    BigDecimal used = leaveRequestRepository
                            .findByEmployeeId(employeeId)
                            .stream()
                            .filter(req -> "APPROVED".equals(req.getStatus()))
                            .filter(req -> FiscalYearUtil.fiscalYearFor(req.getStartDate()).equals(fiscalYear))
                            .map(LeaveRequest::getRequestedDays)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // 4. Remaining balance (carry forward included automatically)
                    BigDecimal balance = accrued.subtract(used);
                    if (balance.compareTo(BigDecimal.ZERO) < 0) {
                        balance = BigDecimal.ZERO; // no negatives
                    }

                    return LeaveBalanceDto.builder()
                            .employeeId(employeeId)
                            .fiscalYear(fiscalYear)
                            .leaveType(type)
                            .balance(balance.setScale(2, RoundingMode.HALF_UP))
                            .build();
                })
                .collect(Collectors.toList());

        response.setStatus("SUCCESS");
        response.setMessage("Leave balances fetched successfully");
        response.setData(balances);
        response.setStatusCode(200);
        response.setResponse_message("Process Executed success");

        return response;
    }




    // Get Leave History of An Employee - API
    public Response getLeaveHistory(Long empId){
        Employee employee = this.employeeRepository.findById(empId)
                .orElseThrow(() -> new NoDataExist("Employee not found with id: " + empId));

        List<LeaveRequest> leaves = this.leaveRequestRepository.findByEmployeeId(empId);

        response.setStatus("success");
        response.setMessage("Leave history fetched successfully");
        response.setData(leaves.stream().map((l)-> this.mapper.map(l,LeaveRequestDto.class)).collect(Collectors.toList()));
        response.setStatusCode(200);
        response.setResponse_message("Process executed success");

        return response;
    }

    // Reject a Leave - API
    public Response rejectLeave(Long leaveId, Long approverId, String reason) {
        LeaveRequest leave = this.leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new NoDataExist("Leave not found with id: " + leaveId));

        // ensure leave is still pending
        if (!leave.getStatus().equals(LeaveStatus.PENDING)) {
            response.setStatus("FAILED");
            response.setMessage("Leave status is not PENDING");
            response.setData(null);
            response.setStatusCode(404);
            response.setResponse_message("Process executed success");
            return response;
        }

        // verify approver exists
        this.employeeRepository.findById(approverId)
                .orElseThrow(() -> new NoDataExist("Approver not found with id: " + approverId));

        // update leave status
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(approverId);
        leave.setApprovedAt(LocalDateTime.now());
        leave.setReason(reason != null ? reason : "No reason provided");

        LeaveRequest saved = this.leaveRequestRepository.save(leave);

        response.setStatus("SUCCESS");
        response.setMessage("Leave rejected successfully");
        response.setData(this.mapper.map(saved, LeaveRequestDto.class));
        response.setStatusCode(200);
        response.setResponse_message("Process executed success");
        return response;
    }

    public Response getAllLeave(){
        List<LeaveRequest> allLeave = this.leaveRequestRepository.findAll();

        response.setStatus("SUCCESS");
        response.setMessage("Leave fetched successfully");
        response.setData(allLeave.stream().map((leave)-> this.mapper.map(leave, LeaveRequestDto.class)).collect(Collectors.toList()));
        response.setStatusCode(200);
        response.setResponse_message("Process executed success");
        return response;
    }

}

