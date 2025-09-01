package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // POST Request to add a new Product
    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(@Valid @RequestBody ProductDto productDto){
        log.info("Add product in controller");
        Response response = this.productService.addProduct(productDto);
        log.info("Add product in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all product
    @GetMapping("/getAllProduct")
    public ResponseEntity<Response> getAllProduct(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get all product in controller");

        Response response = this.productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        log.info("Get all product in controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing product
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductDto productDto){
        log.info("Update product in controller");
        Response response = this.productService.updateProduct(id, productDto);
        log.info("Update product in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove a product
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){
        log.info("Delete Product in controller");
        Response response = this.productService.deleteProduct(id);
        log.info("Delete Product in controller executed");

        return ResponseEntity.ok(response);
    }

    // POST Request to assign a project to an employee
    @PostMapping("/{productId}/assign-employee/{employeeId}")
    public ResponseEntity<Response> assignProduct(@PathVariable Long productId,
                                                  @PathVariable Long employeeId){
        log.info("Assign product in controller");
        Response response = this.productService.assignProductToEmployee(productId, employeeId);
        log.info("Assign product in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove a product from employee
    @DeleteMapping("/{productId}/remove-employee/{employeeId}")
    public ResponseEntity<Response> removeProduct(@PathVariable Long productId,
                                                  @PathVariable Long employeeId){
        log.info("Remove Product in controller");
        Response response = this.productService.removeProductFromEmployee(productId, employeeId);
        log.info("Remove product in controller executed");

        return ResponseEntity.ok(response);
    }

}
