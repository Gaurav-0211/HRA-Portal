package com.hra.hra.repository;

import com.hra.hra.config.LeaveType;
import com.hra.hra.entity.LeaveTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface LeaveTransactionRepository extends JpaRepository<LeaveTransaction, Long> {

    boolean existsByEmployeeIdAndTransactionTypeAndYearMonth(Long employeeId, String transactionType, String yearMonth);
    boolean existsByEmployeeIdAndTransactionTypeAndYearMonthAndLeaveType(
            Long employeeId, String transactionType, String yearMonth, LeaveType leaveType);

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM LeaveTransaction t WHERE t.employee.id = :employeeId AND t.fiscalYear = :fy AND t.leaveType = :lt AND t.transactionType = 'ACCRUAL'")
    BigDecimal sumAccrualsByEmployeeAndFiscalYearAndLeaveType(Long employeeId, String fy, LeaveType lt);

}
