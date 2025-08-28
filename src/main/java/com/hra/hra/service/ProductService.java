package com.hra.hra.service;

import com.hra.hra.dto.ProductDto;
import com.hra.hra.dto.Response;

public interface ProductService {

    Response addProduct(ProductDto productDto);

    Response getAllProduct();

    Response deleteProduct(Long id);

    Response updateProduct(Long id, ProductDto productDto);

}
