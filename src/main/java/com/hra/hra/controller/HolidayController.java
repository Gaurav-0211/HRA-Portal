package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.HolidayDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    // POST request to add a holiday
    @PostMapping("/addHoliday")
    public ResponseEntity<Response> addHoliday(@RequestBody HolidayDto holidayDto){
        log.info("Add Holiday in controller");
        Response response = this.holidayService.addHoliday(holidayDto);
        log.info("Add Holiday in controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT Request to update a holiday
    @PutMapping("/updateHoliday/{id}")
    public ResponseEntity<Response> updateHoliday(@PathVariable Long id, @RequestBody HolidayDto holidayDto){
        log.info("Update holiday in controller");
        Response response = this.holidayService.updateHoliday(id, holidayDto);
        log.info("Update Holiday in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE request to remove a holiday
    @DeleteMapping("/deleteHoliday/{id}")
    public ResponseEntity<Response> deleteHoliday(@PathVariable Long id){
        log.info("Delete Holiday in controller");
        Response response = this.holidayService.deleteHoliday(id);

        log.info("Delete Holiday in controller executed");


        return ResponseEntity.ok(response);
    }

    // GET request to get a holiday
    @GetMapping("/getHoliday")
    public ResponseEntity<Response> getAllHoliday(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get All Holiday in controller");

        Response response = this.holidayService.getAllHoliday(pageNumber, pageSize, sortBy, sortDir);
        log.info("Get all Holiday in controller executed");

        return ResponseEntity.ok(response);
    }

}
