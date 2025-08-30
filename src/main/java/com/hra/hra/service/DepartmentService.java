package com.hra.hra.service;

import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.PageResponse;
import com.hra.hra.dto.Response;

import java.util.List;

public interface DepartmentService {
    Response addDepartment(DepartmentDto departmentDto);

    Response getAllDepartment(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Response deleteDepartment(Long id);

    Response updateDepartment(Long id,DepartmentDto departmentDto);

    Response getDepartmentById(Long id);
}
