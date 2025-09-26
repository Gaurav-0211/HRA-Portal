package com.hra.hra.entity;

import com.hra.hra.config.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Employee employee;

    private String fiscalYear;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Column(precision = 6, scale = 2)
    private BigDecimal amount; // positive for accrual, negative for deduction

    private String transactionType; // ACCRUAL, DEDUCTION, UNPAID_MARK, ADJUSTMENT

    @Column(name = "year_month_col")
    private String yearMonth;

    private LocalDateTime createdAt;

    @Column(length = 500)
    private String note;
}
