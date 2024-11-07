package com.starline.data.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/7/2024 12:52 PM
@Last Modified 11/7/2024 12:52 PM
Version 1.0
*/

import com.starline.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    Optional<Product> findByCode(String code);
}
