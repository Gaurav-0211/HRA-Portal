package com.hra.hra.entity;

import com.hra.hra.config.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_balances", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id","fiscalYear","leaveType"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) 
    private Employee employee;

    private String fiscalYear; // "2025-2026"

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Column(precision = 6, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    private LocalDateTime lastUpdated;
}