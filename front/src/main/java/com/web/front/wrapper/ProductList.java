package com.web.front.wrapper;

import com.web.front.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
    private List<Product> productList;

    public ProductList() {
        productList = new ArrayList<>();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
