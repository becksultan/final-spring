package com.web.front.entity;

import java.util.List;

public class Cart {
    private Long id;
    private List<Long> products;
    private double total;

    public Cart() {}

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

    public void addProduct(Long product_id) {
        this.products.add(product_id);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", products=" + products +
                ", total=" + total +
                '}';
    }
}
