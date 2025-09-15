package com.hra.hra.service;

import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.Response;

public interface DepartmentService {
    Response addDepartment(DepartmentDto departmentDto);

    Response getAllDepartment(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Response deleteDepartment(Long id);

    Response updateDepartment(Long id,DepartmentDto departmentDto);

    Response getDepartmentById(Long id);

    Response getEmployeesOfDepartment(Long id);

    Response getDepartmentAndEmployeeCount(Long id);

    Response getDepartmentByName(String name);
}
