package com.hra.hra.service;

import com.hra.hra.dto.HolidayDto;
import com.hra.hra.dto.Response;

public interface HolidayService {

    Response addHoliday(HolidayDto holidayDto);

    Response updateHoliday(Long id, HolidayDto holidayDto);

    Response deleteHoliday(Long id);

    Response getAllHoliday();
}
