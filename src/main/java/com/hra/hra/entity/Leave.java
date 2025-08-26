package com.hra.hra.entity;

import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private LocalDate appliedOn;

    private LocalDate decisionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status; // PENDING, APPROVED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type; // CASUAL, SICK

    // Mapping to each leave will belong to an employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

}
