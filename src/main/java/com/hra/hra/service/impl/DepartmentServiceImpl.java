package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Department;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.exception.NoDepartmentExist;
import com.hra.hra.repository.DepartmentRepository;
import com.hra.hra.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to add a new department
    @Override
    public Response addDepartment(DepartmentDto departmentDto) {
        log.info("Add department in dept-service Impl");
        Department department = this.mapper.map(departmentDto, Department.class);
        this.repository.save(department);
        // Sending a proper response
        response.setStatus("SUCCESS");
        response.setMessage("Department added successfully");
        response.setData(this.mapper.map(department, DepartmentDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Execution process completed");
        log.info("add department in service impl executed");
        return response;
    }

    // API to fetch all departments
    @Override
    public Response getAllDepartment() {
        log.info("Get all Department in service Impl");
        List<Department> departments= this.repository.findAll();
        if(departments == null){
            throw new NoDepartmentExist("No department is inserted till now");
        }
        response.setStatus("SUCCESS");
        response.setMessage("All Department fetched successfully");
        response.setData(departments.stream().map((dept)->this.mapper.map(dept,DepartmentDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("get all department in service impl executed");
        return response;
    }

    // API to delete a department
    @Override
    public Response deleteDepartment(Long id) {
        log.info("Delete a department in service impl");
        Department department = this.repository.findById(id)
                .orElseThrow(()-> new NoDepartmentExist("No department is present with given id "+id));
        this.repository.delete(department);
        response.setStatus("SUCCESS");
        response.setMessage("Department deleted successfully");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("delete department in service impl executed");
        return response;
    }

    // API to update an existing department
    @Override
    public Response updateDepartment(Long id, DepartmentDto departmentDto) {
        log.info("Update department in service Impl");
        Department department = this.repository.findById(id)
                .orElseThrow(()-> new NoDataExist("No department exist with the given id"));
        this.mapper.map(departmentDto,Department.class);
        Department saved = this.repository.save(department);
        response.setStatus("SUCCESS");
        response.setMessage("Department updated successfully");
        response.setData(this.mapper.map(saved, DepartmentDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("update department in service impl executed");
        return response;
    }
}
