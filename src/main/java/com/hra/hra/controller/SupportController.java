package com.hra.hra.controller;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.SupportDto;
import com.hra.hra.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supports")
public class SupportController {

    @Autowired
    private SupportService supportService;

    //POST request to raise a new support query
    @PostMapping("/raiseQuery")
    public ResponseEntity<Response> raisedQuery(@RequestBody SupportDto supportDto){
        Response response = this.supportService.addQuery(supportDto);

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing query
    @PutMapping("/updateQuery/{id}")
    public ResponseEntity<Response> updateQuery(@PathVariable Long id, @RequestBody SupportDto supportDto){
        Response response = this.supportService.updateQuery(id, supportDto);

        return ResponseEntity.ok(response);
    }

    // GET request to fetch all query raised by an employee
    @GetMapping("/getQueryByEmployeeId/{id}")
    public ResponseEntity<Response> getQueryByEmpId(@PathVariable Long id){
        Response response = this.supportService.getSupportByEmployeeId(id);

        return ResponseEntity.ok(response);
    }

    // GET request to get all query raised by all employee
    @GetMapping("/getAllQuery")
    public ResponseEntity<Response> getAllQuery(){
        Response response = this.supportService.getAllSupports();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteQuery/{id}")
    public ResponseEntity<Response> deleteQuery(@PathVariable Long id){
        Response response = this.supportService.deleteQuery(id);

        return ResponseEntity.ok(response);
    }

}
