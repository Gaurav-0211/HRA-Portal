package com.hra.hra.service;

import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;

public interface ProductService {

    Response addProduct(ProductDto productDto);

    Response getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Response deleteProduct(Long id);

    Response updateProduct(Long id, ProductDto productDto);

    Response assignProductToEmployee(Long productId, Long employeeId);

    Response removeProductFromEmployee(Long productId, Long employeeId);

}
