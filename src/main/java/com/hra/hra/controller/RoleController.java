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
        RoleDto dto = this.roleService.addRole(roleDto);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Role added success",
                dto,
                200,
                "Execution done success"
        );
        return ResponseEntity.ok(response);
    }

    // PUT Request to update an existing Role
    @PutMapping("/updateRole")
    ResponseEntity<Response> updateRole(@RequestBody RoleDto roleDto){
        RoleDto dto = this.roleService.updateRole(roleDto);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Role Updated successfully",
                dto,
                200,
                "Execution done"
        );
        return ResponseEntity.ok(response);
    }

    // DELETE Request to delete a Role
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteRole(@RequestParam Long id){
        this.roleService.deleteRole(id);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Role Deleted Success",
                null,
                200,
                "Execution Done"
        );
        return ResponseEntity.ok(response);
    }

    // GET Request to Fetch all Roles from DB
    @GetMapping("/getAll")
    ResponseEntity<Response> getAllRole(){
       List<RoleDto> roles =  this.roleService.getAllRole();
       Response response = Response.buildResponse(
               "SUCCESS",
               "All Role Fetched",
               roles,
               200,
               "Execution complete"
       );
       return ResponseEntity.ok(response);
    }

}
