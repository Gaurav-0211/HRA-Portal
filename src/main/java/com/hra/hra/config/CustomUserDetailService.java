package com.hra.hra.config;

import com.hra.hra.entity.Employee;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = this.employeeRepository.findByEmail(username);
        if (employee == null) {
            throw new NoDataExist("User not found with email: " + username);
        }

        String roleName = "ROLE_" + employee.getEmployeeRole().getName();

        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(roleName))
        );
    }
}
