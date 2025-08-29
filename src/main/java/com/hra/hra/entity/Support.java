package com.hra.hra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "supports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issueType; // Technical, HR, Payroll

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String status; // OPEN , IN_PROGRESS, RESOLVED

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Default Status and date
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
        this.status = "OPEN";
    }
}
