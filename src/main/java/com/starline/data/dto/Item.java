package com.starline.data.dto;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/6/2024 11:33 AM
@Last Modified 11/6/2024 11:33 AM
Version 1.0
*/

public class Item {

    private String name;
    private Double price;

    private Integer quantity;

    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
