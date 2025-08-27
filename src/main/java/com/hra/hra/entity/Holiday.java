package com.hra.hra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "holiday")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String occasion;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String type;
}
