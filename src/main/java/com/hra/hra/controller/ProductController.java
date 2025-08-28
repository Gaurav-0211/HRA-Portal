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

    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(@RequestBody ProductDto productDto){
        Response response = this.productService.addProduct(productDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Response> getAllProduct(){
        Response response = this.productService.getAllProduct();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Response response = this.productService.updateProduct(id, productDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){
        Response response = this.productService.deleteProduct(id);

        return ResponseEntity.ok(response);
    }

}
