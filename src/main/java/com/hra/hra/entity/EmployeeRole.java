package com.hra.hra.entity;

import com.hra.hra.config.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRole {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;

    public EmployeeRole(RoleType name) {
        this.name = name;
    }
}
