package com.oasys.ProductService.service;

import com.oasys.ProductService.entity.Product;

import com.oasys.ProductService.repository.ProductRepository;
import com.oasys.common_module.clients.external.model.ProductRequest;
import com.oasys.common_module.clients.external.model.ProductResponse;
import com.oasys.common_module.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(
                        "Product with given id not found",
                        "PRODUCT_NOT_FOUND",
                        404));

        ProductResponse productResponse
                = new ProductResponse();

        copyProperties(product, productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found", "NOT_FOUND", 404));

        if (product.getQuantity() < quantity) {
            // Replaced broken builder usage with direct CustomException
            throw new CustomException("Product does not have sufficient Quantity", "INSUFFICIENT_QUANTITY", 409);
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

}
