package com.hra.hra.controller;

import com.hra.hra.dto.HolidayDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    // POST request to add a holiday
    @PostMapping("/addHoliday")
    public ResponseEntity<Response> addHoliday(@RequestBody HolidayDto holidayDto){
        Response response = this.holidayService.addHoliday(holidayDto);

        return ResponseEntity.ok(response);
    }

    // PUT Request to update a holiday
    @PutMapping("/updateHoliday/{id}")
    public ResponseEntity<Response> updateHoliday(@PathVariable Long id, @RequestBody HolidayDto holidayDto){
        Response response = this.holidayService.updateHoliday(id, holidayDto);

        return ResponseEntity.ok(response);
    }

    // DELETE request to remove a holiday
    @DeleteMapping("/deleteHoliday/{id}")
    public ResponseEntity<Response> deleteHoliday(@PathVariable Long id){
        Response response = this.holidayService.deleteHoliday(id);

        return ResponseEntity.ok(response);
    }

    // GET request to get a holiday
    @GetMapping("/getHoliday")
    public ResponseEntity<Response> getAllHoliday(){
        Response response = this.holidayService.getAllHoliday();

        return ResponseEntity.ok(response);
    }

}
