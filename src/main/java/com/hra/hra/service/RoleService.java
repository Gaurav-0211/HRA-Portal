package com.hra.hra.service;

import com.hra.hra.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto addRole(RoleDto roleDto);

    void deleteRole(Long id);

    RoleDto updateRole(RoleDto roleDto);

    List<RoleDto> getAllRole();
}
