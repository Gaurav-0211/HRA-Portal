package com.hra.hra.service;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.SupportDto;

public interface SupportService {

    Response addQuery(SupportDto supportDto);

    Response deleteQuery(Long id);

    Response updateQuery(Long id, SupportDto supportDto);

    Response getSupportByEmployeeId(Long employeeId); // all queries by an employee

    Response getAllSupports(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Response handleSupportResolved(Long id);

    Response handleSupportInProgress(Long id);

}
