package com.web.cart.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> products;
    private double total;

    public Cart() {
        this.total = 0;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getProducts() {
        return products;
    }

    public double getTotal() {
        return total;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
