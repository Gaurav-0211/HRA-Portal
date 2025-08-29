package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.Response;
import com.hra.hra.dto.SupportDto;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Support;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.SupportRepository;
import com.hra.hra.service.SupportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to add a new Query
    @Override
    public Response addQuery(SupportDto supportDto) {
        Employee employee = this.employeeRepository.findById(supportDto.getEmployeeId())
                .orElseThrow(() -> new NoDataExist("Employee not found"));

        Support support = this.mapper.map(supportDto, Support.class);
        support.setEmployee(employee);
        Support saved= this.supportRepository.save(support);

        response.setStatus("SUCCESS");
        response.setMessage("Query raised successfully");
        response.setData(this.mapper.map(saved, SupportDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process execution success");

        return response;
    }

    // API to delete or remove a Query
    @Override
    public Response deleteQuery(Long id) {
        Support support = this.supportRepository.findById(id)
                .orElseThrow(()->new NoDataExist("No query found with given ID "+id));

        this.supportRepository.delete(support);

        response.setStatus("SUCCESS");
        response.setMessage("Query deleted successfully");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");

        return response;
    }

    // API to update an existing support
    @Override
    public Response updateQuery(Long id, SupportDto supportDto) {
        Support support = this.supportRepository.findById(id)
                .orElseThrow(() -> new NoDataExist("Support not found"));
        support.setIssueType(supportDto.getIssueType());
        support.setDescription(supportDto.getDescription());
        support.setStatus(supportDto.getStatus());

        response.setStatus("SUCCESS");
        response.setMessage("Query updated successfully");
        response.setData(this.mapper.map(support, SupportDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");

        return response;

    }

    // API to get all support raised by an employee id
    @Override
    public Response getSupportByEmployeeId(Long employeeId) {
        Object  data =  this.supportRepository.findByEmployeeId(employeeId)
                .stream()
                .map(s -> {
                    SupportDto dto = this.mapper.map(s, SupportDto.class);
                    dto.setEmployeeId(s.getEmployee().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setStatus("SUCCESS");
        response.setMessage("Query fetched successfully");
        response.setData(data);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");

        return response;

    }

    // API to get all support raised by all employee
    @Override
    public Response getAllSupports() {
        Object data =  this.supportRepository.findAll()
                .stream()
                .map(s -> {
                    SupportDto dto = this.mapper.map(s, SupportDto.class);
                    dto.setEmployeeId(s.getEmployee().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setStatus("SUCCESS");
        response.setMessage("Query fetched successfully");
        response.setData(data);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");

        return response;
    }
}
