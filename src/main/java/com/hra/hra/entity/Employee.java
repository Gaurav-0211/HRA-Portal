package com.hra.hra.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "employee")
public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Many Employee can have same role
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Many employee can belong to same department
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // One Employee can apply many leaves
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leave> leaves = new ArrayList<>();

    // Many employee can work on multiple project
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonManagedReference
    private Set<Project> projects = new HashSet<>();

    public void addProject(Project project) {
        this.projects.add(project);
        project.getEmployees().add(this); // maintain both sides mappings
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getEmployees().remove(this);
    }

    // Many-to-Many with Product Entity
    @ManyToMany
    @JoinTable(
            name = "employee_products",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_role")
    private EmployeeRole employeeRole;


    // Below three is for sending and validating link to forgot password while sending a link on mail
    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private LocalDateTime lastResetLinkSentAt;

    // Below two is to track login attempts and lock login
    private int failedLoginAttempts;

    private LocalDateTime accountLockedUntil;


    // User Details methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
