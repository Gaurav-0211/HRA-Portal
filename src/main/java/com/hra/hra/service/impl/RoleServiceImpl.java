package com.hra.hra.service.impl;

import com.hra.hra.dto.RoleDto;
import com.hra.hra.entity.Role;
import com.hra.hra.exception.EmployeeAlreadyExist;
import com.hra.hra.exception.NoRoleExist;
import com.hra.hra.repository.RoleRepository;
import com.hra.hra.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;


    // API to add new Role
    @Override
    public RoleDto addRole(RoleDto roleDto) {
        log.info("Add new role in RoleService Impl");
        Role role = this.roleRepository.getByRoleName(roleDto.getRoleName());
        if(role != null){
            throw new EmployeeAlreadyExist("This role is already exist");
        }
        role =  mapper.map(roleDto, Role.class);
        this.roleRepository.save(role);
        return mapper.map(role, RoleDto.class);
    }

    // API to delete Role
    @Override
    public void deleteRole(Long id) {
        log.info("Delete Role in RoleServiceImpl");
        Role role = this.roleRepository.findById(id)
                .orElseThrow(()->new NoRoleExist("No role found with given Id"));
        this.roleRepository.delete(role);
    }

    // API to update an existing role
    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Role role = this.roleRepository.getByRoleName(roleDto.getRoleName());
        if(role == null){
            throw new NoRoleExist("No role exist with given Role Name");
        }
        role = this.mapper.map(roleDto, Role.class);
        this.roleRepository.save(role);
        return this.mapper.map(role, RoleDto.class);
    }

    // API to get all Roles
    @Override
    public List<RoleDto> getAllRole() {
        log.info("Get all role api in Role Service Impl");
        List<Role> roles = this.roleRepository.findAll();
        if(roles == null){
            throw new NoRoleExist("No role exist till now");
        }
        return roles.stream().map((role)->this.mapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }
}
