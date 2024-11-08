package com.starline.data;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/8/2024 1:45 PM
@Last Modified 11/8/2024 1:45 PM
Version 1.0
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "total_amount")
    @Comment(value = "Total transaction amount", on = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    @Comment(value = "Total discount", on = "discount")
    private Double discount;

    @Column(name = "tax")
    @Comment(value = "Tax applied to the transaction", on = "tax")
    private Double tax;

    @Column(name = "net_amount")
    @Comment(value = "Final amount after tax and discounts", on = "net_amount")
    private Double netAmount;

    @Column(name = "payment_type")
    @Comment(value = "Type of payment (e.g., Cash, Credit Card, etc.)", on = "payment_type")
    private String paymentType;


    @OneToMany(mappedBy = "transaction")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<TransactionItem> items;

}
