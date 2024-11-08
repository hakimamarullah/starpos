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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Modifying
    public void saveBatch(Set<Product> products) {
        productRepository.saveAllAndFlush(products);
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

    public Page<Product> findByCodeOrNameContains(String searchKey, Pageable pageable) {
        return productRepository.findByCodeContainsIgnoreCaseOrNameContainsIgnoreCase(searchKey, searchKey, pageable);
    }

}
