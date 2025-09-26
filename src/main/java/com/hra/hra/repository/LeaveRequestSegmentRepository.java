package com.hra.hra.repository;

import com.hra.hra.entity.LeaveRequestSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LeaveRequestSegmentRepository extends JpaRepository<LeaveRequestSegment, Long> {

    @Query("SELECT COALESCE(SUM(s.paidDays), 0) FROM LeaveRequestSegment s " +
           "WHERE s.leaveRequest.employee.id = :employeeId AND s.yearMonth = :yearMonth " +
           "AND s.leaveRequest.status = 'APPROVED'")
    BigDecimal sumPaidDaysByEmployeeAndYearMonth(@Param("employeeId") Long employeeId,
                                                 @Param("yearMonth") String yearMonth);
}
