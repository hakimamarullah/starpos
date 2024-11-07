package com.starline.data;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/6/2024 11:27 AM
@Last Modified 11/6/2024 11:27 AM
Version 1.0
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Product extends AbstractEntity {

    private String name;
    private String description;
    private Double price;

    @Column(unique = true)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
