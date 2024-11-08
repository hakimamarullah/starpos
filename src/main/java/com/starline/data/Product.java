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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Table(indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_code", columnList = "code", unique = true)
})
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name should not be blank")
    @Length(max = 255, message = "Name can't be exceeded 255 in length")
    private String name;

    @Length(max = 255, message = "Description can't be exceeded 255 in length")
    private String description;


    @NotNull(message = "Price should not be null")
    @Min(0)
    private Double price;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @NotBlank(message = "Product code is mandatory")
    @Length(max = 30, message = "Product Code maximum length is 30")
    @Column(unique = true, length = 30)
    private String code;

    public Boolean isDeleted() {
        return isDeleted;
    }

    public Product setDeleted(Boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return this.getCode().equalsIgnoreCase(product.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
