package com.hra.hra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
@Getter
@Setter

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();
}
