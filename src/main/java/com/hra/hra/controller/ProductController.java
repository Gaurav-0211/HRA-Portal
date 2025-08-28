package com.hra.hra.controller;

import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // POST Request to add a new Product
    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(@RequestBody ProductDto productDto){
        Response response = this.productService.addProduct(productDto);

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all product
    @GetMapping("/getAllProduct")
    public ResponseEntity<Response> getAllProduct(){
        Response response = this.productService.getAllProduct();

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing product
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Response response = this.productService.updateProduct(id, productDto);

        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove a product
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){
        Response response = this.productService.deleteProduct(id);

        return ResponseEntity.ok(response);
    }

    // POST Request to assign a project to an employee
    @PostMapping("/{productId}/assign-employee/{employeeId}")
    public ResponseEntity<Response> assignProduct(@PathVariable Long productId,
                                                  @PathVariable Long employeeId){
        Response response = this.productService.assignProductToEmployee(productId, employeeId);

        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove a product from employee
    @DeleteMapping("/{productId}/remove-employee/{employeeId}")
    public ResponseEntity<Response> removeProduct(@PathVariable Long productId,
                                                  @PathVariable Long employeeId){
        Response response = this.productService.removeProductFromEmployee(productId, employeeId);

        return ResponseEntity.ok(response);
    }

}
