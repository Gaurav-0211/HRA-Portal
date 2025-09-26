package com.hra.hra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request_segments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveRequestSegment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    @Column(name = "year_month_col")
    private String yearMonth;

    private LocalDate segmentStart;
    private LocalDate segmentEnd;

    @Column(precision = 6, scale = 2)
    private BigDecimal days;

    @Column(precision = 6, scale = 2)
    private BigDecimal paidDays = BigDecimal.ZERO;

    @Column(precision = 6, scale = 2)
    private BigDecimal unpaidDays = BigDecimal.ZERO;
}
