package com.hra.hra.repository;

import com.hra.hra.entity.LeaveTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTransactionRepository extends JpaRepository<LeaveTransaction, Long> {

    boolean existsByEmployeeIdAndTransactionTypeAndYearMonth(Long employeeId, String transactionType, String yearMonth);
}
