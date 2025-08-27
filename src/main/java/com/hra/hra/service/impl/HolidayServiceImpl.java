package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.HolidayDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Holiday;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.HolidayRepository;
import com.hra.hra.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

        return response;
    }

    // API to update existing holiday
    @Override
    public Response updateHoliday(Long id, HolidayDto holidayDto) {
        log.info("Update Holiday in Service Impl triggered");
        Holiday holiday = this.holidayRepository.findById(id)
                .orElseThrow(()->new NoDataExist("No holiday found with given id"));
        this.mapper.map(holidayDto, Holiday.class);
        Holiday updated = this.holidayRepository.save(holiday);

        response.setStatus("SUCCESS");
        response.setMessage("Holiday updated success");
        response.setData(this.mapper.map(updated, HolidayDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution Process Success");

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

        return response;
    }

    // API to fetch all holiday
    @Override
    public Response getAllHoliday() {
        log.info("get all holiday in service impl triggered");
        List<Holiday> holidays = this.holidayRepository.findAll();

        if(holidays == null){
            throw new NoDataExist("No holiday exist");
        }
        response.setStatus("SUCCESS");
        response.setMessage("Holiday fetched success");
        response.setData(holidays.stream().map((h)->this.mapper.map(h, HolidayDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution Process Success");

        return response;
    }
}
