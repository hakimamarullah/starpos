package com.starline.data.dto;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/6/2024 11:33 AM
@Last Modified 11/6/2024 11:33 AM
Version 1.0
*/

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class Item implements Comparable<Item> {
    @Override
    public int compareTo(Item o) {
        return this.getCode().compareTo(o.getCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item item) {
            return this.getCode().equalsIgnoreCase(item.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private String name;
    private Double price;

    private Integer quantity;

    private String code;

}
