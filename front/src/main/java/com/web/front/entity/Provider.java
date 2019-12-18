package com.web.front.entity;

import java.util.List;

public class Provider {
    private Long id;
    private String name;
    private List<Product> products;

    public String getName() {
        return name;
    }

    public Provider(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Provider() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
