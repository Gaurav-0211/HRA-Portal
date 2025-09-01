package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.Response;
import com.hra.hra.dto.SupportDto;
import com.hra.hra.service.SupportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/supports")
public class SupportController {

    @Autowired
    private SupportService supportService;

    //POST request to raise a new support query
    @PostMapping("/raiseQuery")
    public ResponseEntity<Response> raisedQuery(@Valid @RequestBody SupportDto supportDto){
        log.info("Raise query in support controller");
        Response response = this.supportService.addQuery(supportDto);
        log.info("Raise query in support controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing query
    @PutMapping("/updateQuery/{id}")
    public ResponseEntity<Response> updateQuery(@PathVariable Long id,@Valid @RequestBody SupportDto supportDto){
        log.info("Update query in support controller");

        Response response = this.supportService.updateQuery(id, supportDto);
        log.info("Update query in support controller executed");

        return ResponseEntity.ok(response);
    }

    // GET request to fetch all query raised by an employee
    @GetMapping("/getQueryByEmployeeId/{id}")
    public ResponseEntity<Response> getQueryByEmpId(@PathVariable Long id){
        log.info("Get query by employee Id in support controller");

        Response response = this.supportService.getSupportByEmployeeId(id);
        log.info("Get query by employee Id in support controller executed");

        return ResponseEntity.ok(response);
    }

    // GET request to get all query raised by all employee
    @GetMapping("/getAllQuery")
    public ResponseEntity<Response> getAllQuery(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get all query in support controller");
        Response response = this.supportService.getAllSupports(pageNumber, pageSize, sortBy, sortDir);
        log.info("Get all query in support controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE request to delete a query
    @DeleteMapping("/deleteQuery/{id}")
    public ResponseEntity<Response> deleteQuery(@PathVariable Long id){
        log.info("Delete query in support controller");

        Response response = this.supportService.deleteQuery(id);
        log.info("Delete query in support controller executed");

        return ResponseEntity.ok(response);
    }

    // POST request to resolve raised query by HR/manager
    @PostMapping("/resolveQueryId/{id}")
    public ResponseEntity<Response> resolveQuery(@PathVariable Long id){
        log.info("Resolve query in controller");
        Response response = this.supportService.handleSupportResolved(id);
        log.info("Resolve query in controller executed");

        return ResponseEntity.ok(response);
    }

    // POST request to mark a query in-Progress by HR/Manager
    @PostMapping("/inProgressQueryId/{id}")
    public ResponseEntity<Response> inProgressQuery(@PathVariable Long id){
        log.info("Mark in progress Query in controller");
        Response response = this.supportService.handleSupportInProgress(id);
        log.info("Mark in progress Query in controller executed");

        return ResponseEntity.ok(response);
    }

}
