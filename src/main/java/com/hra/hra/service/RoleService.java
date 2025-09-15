package com.hra.hra.service;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.RoleDto;

public interface RoleService {
    Response addRole(RoleDto roleDto);

    Response deleteRole(Long id);

    Response updateRole(Long id,RoleDto roleDto);

    Response getAllRole();
}
