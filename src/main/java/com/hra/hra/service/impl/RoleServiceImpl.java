package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.Response;
import com.hra.hra.dto.RoleDto;
import com.hra.hra.entity.Role;
import com.hra.hra.exception.EmployeeAlreadyExist;
import com.hra.hra.exception.NoDataExist;
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

    @Autowired
    private Response response;


    // API to add new Role
    @Override
    public Response addRole(RoleDto roleDto) {
        log.info("Add new role in RoleService Impl");
        Role role = this.roleRepository.getByRoleName(roleDto.getRoleName());
        if(role != null){
            throw new EmployeeAlreadyExist("This role is already exist");
        }
        role =  mapper.map(roleDto, Role.class);
        this.roleRepository.save(role);
        // Return a response
        response.setStatus("SUCCESS");
        response.setMessage("Role added successfully");
        response.setData(mapper.map(role, RoleDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Execution process completed");
        return response;
    }

    // API to delete Role
    @Override
    public Response deleteRole(Long id) {
        log.info("Delete Role in RoleServiceImpl");
        Role role = this.roleRepository.findById(id)
                .orElseThrow(()->new NoRoleExist("No role found with given Id"));
        this.roleRepository.delete(role);
        response.setStatus("SUCCESS");
        response.setMessage("Role deleted successfully");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");

        return response;
    }

    // API to update an existing role
    @Override
    public Response updateRole(Long id, RoleDto roleDto) {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No Role exist with the given role Id"));
        this.mapper.map(roleDto, Role.class);
        Role saved = this.roleRepository.save(role);

        response.setStatus("SUCCESS");
        response.setMessage("Role updated successfully");
        response.setData(mapper.map(saved, RoleDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        return response;
    }

    // API to get all Roles
    @Override
    public Response getAllRole() {
        log.info("Get all role api in Role Service Impl");
        List<Role> roles = this.roleRepository.findAll();
        if(roles == null){
            throw new NoRoleExist("No role exist till now");
        }

        response.setStatus("SUCCESS");
        response.setMessage("All Role fetched successfully");
        response.setData(roles.stream().map((role) -> this.mapper.map(role, RoleDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        return response;
    }
}
