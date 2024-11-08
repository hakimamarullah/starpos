package com.starline.data;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/8/2024 1:46 PM
@Last Modified 11/8/2024 1:46 PM
Version 1.0
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Table(name = "transaction_item")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "price")
    @Comment(value = "Base price", on = "price")
    private Double price;

    @Column(name = "discount")
    @Comment(value = "Discount amount", on = "discount")
    private Double discount;

    @Column(name = "total_price")
    @Comment(value = "Total price after discount", on = "total_price")
    private Double totalPrice;

    @Column(name = "quantity")
    @Comment(value = "Quantity of the item purchased", on = "quantity")
    private Integer quantity;

    @OneToOne
    @Comment(value = "Foreign Key to product column id", on = "product_id")
    private Product product;

    @ManyToOne
    @Comment(value = "Foreign Key to transaction column id", on = "transaction_id")
    private Transaction transaction;

}
