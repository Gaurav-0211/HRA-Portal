package com.hra.hra.service;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.RoleDto;

import java.util.List;

public interface RoleService {
    Response addRole(RoleDto roleDto);

    Response deleteRole(Long id);

    Response updateRole(RoleDto roleDto);

    Response getAllRole();
}
