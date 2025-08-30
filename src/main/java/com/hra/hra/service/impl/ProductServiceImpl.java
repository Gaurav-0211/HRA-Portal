package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Product;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.ProductRepository;
import com.hra.hra.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    @Autowired
    private EmployeeRepository employeeRepository;

    // API to add a new Product
    @Override
    public Response addProduct(ProductDto productDto) {
        log.info("Add product in service impl");
        Product product = this.mapper.map(productDto, Product.class);
        this.productRepository.save(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product added success");
        response.setData(this.mapper.map(product, ProductDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process Executed success");
        log.info("Add product in service impl executed");

        return response;
    }

    // Get api to fetch all products
    @Override
    public Response getAllProduct() {
        log.info("Get all product in service impl");

        List<Product> products = this.productRepository.findAll();

        response.setStatus("SUCCESS");
        response.setMessage("All Products fetched success");
        response.setData(products.stream().map((p)-> this.mapper.map(p, ProductDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");

        log.info("Get all product in service impl executed");

        return response;
    }

    // API to delete an existing project
    @Override
    public Response deleteProduct(Long id) {
        log.info("Delete product in service impl");

        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No product found with given ID "+id));

        this.productRepository.delete(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product deleted success");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");
        log.info("Delete product in service impl executed");

        return response;
    }

    // API to update an existing product
    @Override
    public Response updateProduct(Long id, ProductDto productDto) {
        log.info("Update product in service impl");

        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No Product found with given Id "+id));

        this.mapper.map(productDto, Product.class);
        Product saved = this.productRepository.save(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product updated success");
        response.setData(this.mapper.map(saved, ProductDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process Executed success");
        log.info("Update product in service impl executed");


        return response;
    }

    // API to assign product to an employee
    @Override
    public Response assignProductToEmployee(Long productId, Long employeeId) {
        log.info("Assign product to employee product in service impl");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoDataExist("Product not found with id: " + productId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataExist("Employee not found with id: " + employeeId));

        // Add mapping
        product.getEmployees().add(employee);
        employee.getProducts().add(product);

        this.productRepository.save(product); // persist relationship
        Employee saved = this.employeeRepository.save(employee);

        response.setStatus("SUCCESS");
        response.setMessage("Product assigned success");
        response.setData(this.mapper.map(saved, EmployeeDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");
        log.info("Assign product to employee product in service impl executed");

        return response;
    }

    @Override
    public Response removeProductFromEmployee(Long productId, Long employeeId) {
        log.info("Remove product to employee product in service impl");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoDataExist("Product not found with id: " + productId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataExist("Employee not found with id: " + employeeId));

        // Remove mapping
        product.getEmployees().remove(employee);
        employee.getProducts().remove(product);

        productRepository.save(product);
        Employee saved = employeeRepository.save(employee);

        response.setStatus("SUCCESS");
        response.setMessage("Product removed success");
        response.setData(this.mapper.map(saved, EmployeeDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");
        log.info("Remove product to employee product in service impl executed");

        return response;
    }
}
