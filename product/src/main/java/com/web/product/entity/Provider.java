package com.web.product.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Provider {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    public Provider() {
    }

    public Provider(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
