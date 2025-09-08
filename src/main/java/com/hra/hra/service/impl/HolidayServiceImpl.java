package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.HolidayDto;
import com.hra.hra.dto.PageResponse;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Holiday;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.HolidayRepository;
import com.hra.hra.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to add a Holiday
    @Override
    public Response addHoliday(HolidayDto holidayDto) {
        log.info("Add Holiday in service Impl triggered");
        Holiday holiday = this.mapper.map(holidayDto, Holiday.class);
        this.holidayRepository.save(holiday);

        response.setStatus("SUCCESS");
        response.setMessage("Holiday added success");
        response.setData(this.mapper.map(holiday, HolidayDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Execution Process Success");
        log.info("Add Holiday in service Impl executed");

        return response;
    }

    // API to update existing holiday
    @Override
    public Response updateHoliday(Long id, HolidayDto holidayDto) {
        log.info("Update Holiday in Service Impl triggered");
        Holiday holiday = this.holidayRepository.findById(id)
                .orElseThrow(()->new NoDataExist("No holiday found with given id"));
        this.mapper.map(holidayDto, holiday);
        Holiday updated = this.holidayRepository.save(holiday);

        response.setStatus("SUCCESS");
        response.setMessage("Holiday updated success");
        response.setData(this.mapper.map(updated, HolidayDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution Process Success");
        log.info("Update Holiday in Service Impl executed");

        return response;
    }

    // API to delete a holiday
    @Override
    public Response deleteHoliday(Long id) {
        log.info("delete holiday in service impl triggered");
        Holiday holiday = this.holidayRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No holiday found with given Id"));

        this.holidayRepository.delete(holiday);

        response.setStatus("SUCCESS");
        response.setMessage("Holiday deleted success");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution Process Success");
        log.info("delete holiday in service impl executed");

        return response;
    }

    // API to fetch all holiday
    @Override
    public Response getAllHoliday(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("get all holiday in service impl triggered");
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Holiday> holidayPage = this.holidayRepository.findAll(pageable);

        List<HolidayDto> holidayDtoList = holidayPage
                .getContent()
                .stream()
                .map(holiday -> mapper.map(holiday, HolidayDto.class))
                .collect(Collectors.toList());

        log.info("All Holiday converted into pages in holiday service impl");
        PageResponse<HolidayDto> obj = PageResponse.<HolidayDto>builder()
                .content(holidayDtoList)
                .pageNumber(holidayPage.getNumber())
                .pageSize(holidayPage.getSize())
                .totalElements(holidayPage.getTotalElements())
                .totalPage(holidayPage.getTotalPages())
                .lastPage(holidayPage.isLast())
                .build();

        response.setStatus("SUCCESS");
        response.setMessage("All Department fetched successfully");
        response.setData(obj);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("Get all holiday in service Impl executed");

        return response;
    }
}
