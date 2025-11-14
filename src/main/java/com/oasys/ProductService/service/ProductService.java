package com.oasys.ProductService.service;

import com.oasys.common_module.clients.external.model.ProductRequest;
import com.oasys.common_module.clients.external.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
