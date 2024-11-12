package com.starline.services;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/12/2024 8:48 AM
@Last Modified 11/12/2024 8:48 AM
Version 1.0
*/

import com.starline.data.Transaction;
import com.starline.data.repository.TransactionItemRepository;
import com.starline.data.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashierService {

    private final TransactionRepository transactionRepository;

    private final TransactionItemRepository itemRepository;

    public CashierService(TransactionRepository transactionRepository, TransactionItemRepository itemRepository) {
        this.transactionRepository = transactionRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Transaction createTransaction() {
        return transactionRepository.save(new Transaction());
    }
}
