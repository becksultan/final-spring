package com.web.product.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.web.product.entity.Product;
import com.web.product.entity.Provider;
import com.web.product.repository.ProductRepository;
import com.web.product.repository.ProviderRepository;
import com.web.product.wrapper.ProductList;
import com.web.product.wrapper.ProviderList;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    private ProductRepository productRepository;
    private ProviderRepository providerRepository;

    public ProductController(ProductRepository productRepository,
                             ProviderRepository providerRepository) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
    }

    @HystrixCommand(fallbackMethod = "listError")
    @RequestMapping(name = "all", value = "/", method = RequestMethod.GET)
    public ProductList all() {
        ProductList productList = new ProductList();
        productList.setProductList((List<Product>) productRepository.findAll());
        return productList;
    }

    public ProductList listError() {
        return new ProductList();
    }

    public Product productError(Product product) {
        return new Product();
    }

    public Product productError(Long id) {
        Product product = new Product();
        return product;
    }

    @HystrixCommand(fallbackMethod = "productError")
    @RequestMapping(name = "product", value = "/{id}", method = RequestMethod.GET)
    public Product product(@PathVariable Long id) throws ObjectNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Product"));
    }

    @HystrixCommand(fallbackMethod = "productError")
    @RequestMapping(name = "add", value = "/", method = RequestMethod.POST)
    public Product add(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @HystrixCommand(fallbackMethod = "productError")
    @RequestMapping(name = "update", value = "/", method = RequestMethod.PUT)
    public Product update(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @HystrixCommand(fallbackMethod = "deleteError")
    @RequestMapping(name = "delete", value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    public void deleteError(Long id) { }

    @HystrixCommand(fallbackMethod = "providerListError")
    @RequestMapping(name = "provider_list", value = "/provider", method = RequestMethod.GET)
    public ProviderList provider_list() {
        ProviderList providerList = new ProviderList();
        providerList.setProviderList((List<Provider>) providerRepository.findAll());
        return providerList;
    }

    public ProviderList providerListError() {
        return new ProviderList();
    }
}
