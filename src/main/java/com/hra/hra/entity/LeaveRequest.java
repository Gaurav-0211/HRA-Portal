package com.hra.hra.entity;

import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(precision = 6, scale = 2)
    private BigDecimal requestedDays;

    @Column(precision = 6, scale = 2)
    private BigDecimal expectedPaidDays = BigDecimal.ZERO; // pre-computed at request time

    @Column(precision = 6, scale = 2)
    private BigDecimal expectedUnpaidDays = BigDecimal.ZERO;

    @Column(precision = 6, scale = 2)
    private BigDecimal paidDays = BigDecimal.ZERO;   // final after approval

    @Column(precision = 6, scale = 2)
    private BigDecimal unpaidDays = BigDecimal.ZERO; // final after approval

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private Long approvedBy;

    @Column(length = 1000)
    private String reason;
}
