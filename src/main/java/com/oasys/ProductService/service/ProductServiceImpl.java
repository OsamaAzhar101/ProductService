package com.oasys.ProductService.service;

import com.oasys.ProductService.entity.Product;

import com.oasys.ProductService.repository.ProductRepository;
import com.oasys.common_module.clients.external.model.ProductRequest;
import com.oasys.common_module.clients.external.model.ProductResponse;
import com.oasys.common_module.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(
                        () -> CustomException.builder()
                                .errorMessage("Product with given id not found")
                                .errorCode("PRODUCT_NOT_FOUND")
                                .build());

        ProductResponse productResponse
                = new ProductResponse();

        copyProperties(product, productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}", quantity, productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(
                        () -> CustomException.builder()
                                .errorMessage("Product with given id not found")
                                .errorCode("PRODUCT_NOT_FOUND")
                                .build());

        if (product.getQuantity() < quantity) {

            throw CustomException.builder()
                    .errorMessage("Product does not have sufficient Quantity")
                    .errorCode("INSUFFICIENT_QUANTITY")
                    .build();

        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated Successfully");
    }
}
