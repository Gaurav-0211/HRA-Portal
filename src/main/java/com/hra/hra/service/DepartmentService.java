package com.hra.hra.service;

import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.Response;

import java.util.List;

public interface DepartmentService {
    Response addDepartment(DepartmentDto departmentDto);

    Response getAllDepartment();

    Response deleteDepartment(Long id);

    Response updateDepartment(Long id,DepartmentDto departmentDto);
}
