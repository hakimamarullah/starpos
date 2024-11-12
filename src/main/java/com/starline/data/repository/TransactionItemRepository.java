package com.starline.data.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/12/2024 8:49 AM
@Last Modified 11/12/2024 8:49 AM
Version 1.0
*/

import com.starline.data.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Integer> {
}
