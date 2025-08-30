package com.hra.hra.controller;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.RoleDto;
import com.hra.hra.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // POST Request to add a New Role
    @PostMapping("/addRole")
    ResponseEntity<Response> addNewRole(@RequestBody RoleDto roleDto){
        log.info("Add new role in controller");
        Response response = this.roleService.addRole(roleDto);
        log.info("Add new role in controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT Request to update an existing Role
    @PutMapping("/updateRole/{id}")
    ResponseEntity<Response> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto){
        log.info("Update Role By role id in controller");
        Response response = this.roleService.updateRole(id, roleDto);
        log.info("Update Role By role id in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE Request to delete a Role
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteRole(@PathVariable Long id){
        log.info("Delete Role in controller");
        Response response = this.roleService.deleteRole(id);
        log.info("Delete Role in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET Request to Fetch all Roles from DB
    @GetMapping("/getAll")
    ResponseEntity<Response> getAllRole(){
        log.info("Get all Role in Controller");
        Response response =  this.roleService.getAllRole();
        log.info("Get all Role in Controller executed");

        return ResponseEntity.ok(response);
    }

}
