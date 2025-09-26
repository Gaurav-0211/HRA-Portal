package com.hra.hra.repository;

import com.hra.hra.config.LeaveType;
import com.hra.hra.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeIdAndFiscalYearAndLeaveType(Long employeeId, String fiscalYear, LeaveType leaveType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM LeaveBalance b WHERE b.employee.id = :employeeId AND b.fiscalYear = :fiscalYear AND b.leaveType = :leaveType")
    Optional<LeaveBalance> findByEmployeeIdAndFiscalYearAndLeaveTypeForUpdate(@Param("employeeId") Long employeeId,
                                                                             @Param("fiscalYear") String fiscalYear,
                                                                             @Param("leaveType") LeaveType leaveType);
}
