package com.hra.hra.repository;


import com.hra.hra.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getByRoleName(String roleName);
}
