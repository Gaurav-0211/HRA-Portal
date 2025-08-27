package com.hra.hra.controller;

import com.hra.hra.dto.Response;
import com.hra.hra.dto.RoleDto;
import com.hra.hra.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // POST Request to add a New Role
    @PostMapping("/addRole")
    ResponseEntity<Response> addNewRole(@RequestBody RoleDto roleDto){
        Response response = this.roleService.addRole(roleDto);

        return ResponseEntity.ok(response);
    }

    // PUT Request to update an existing Role
    @PutMapping("/updateRole/{id}")
    ResponseEntity<Response> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto){
        Response response = this.roleService.updateRole(id, roleDto);

        return ResponseEntity.ok(response);
    }

    // DELETE Request to delete a Role
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteRole(@PathVariable Long id){
        Response response = this.roleService.deleteRole(id);

        return ResponseEntity.ok(response);
    }

    // GET Request to Fetch all Roles from DB
    @GetMapping("/getAll")
    ResponseEntity<Response> getAllRole(){
        Response response =  this.roleService.getAllRole();

        return ResponseEntity.ok(response);
    }

}
