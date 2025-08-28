package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Product;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.ProductRepository;
import com.hra.hra.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to add a new Product
    @Override
    public Response addProduct(ProductDto productDto) {
        Product product = this.mapper.map(productDto, Product.class);
        this.productRepository.save(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product added success");
        response.setData(this.mapper.map(product, ProductDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process Executed success");

        return response;
    }

    // Get api to fetch all products
    @Override
    public Response getAllProduct() {
        List<Product> products = this.productRepository.findAll();

        response.setStatus("SUCCESS");
        response.setMessage("All Products fetched success");
        response.setData(products.stream().map((p)-> this.mapper.map(p, ProductDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");

        return response;
    }

    // API to delete an existing project
    @Override
    public Response deleteProduct(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No product found with given ID "+id));

        this.productRepository.delete(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product deleted success");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Executed success");

        return response;
    }

    // API to update an existing product
    @Override
    public Response updateProduct(Long id, ProductDto productDto) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No Product found with given Id "+id));

        this.mapper.map(productDto, Product.class);
        Product saved = this.productRepository.save(product);

        response.setStatus("SUCCESS");
        response.setMessage("Product updated success");
        response.setData(this.mapper.map(saved, ProductDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process Executed success");

        return response;
    }
}
