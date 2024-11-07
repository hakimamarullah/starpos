package com.starline.services;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/7/2024 12:53 PM
@Last Modified 11/7/2024 12:53 PM
Version 1.0
*/

import com.starline.data.Product;
import com.starline.data.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> get(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByProductCode(String code) {
        return productRepository.findByCode(code);
    }

    public Page<Product> list(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> listAvailableProduct(Pageable pageable) {
        return productRepository.findByIsDeletedFalse(pageable);
    }

}
