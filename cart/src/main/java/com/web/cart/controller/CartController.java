package com.web.cart.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.List;
import com.web.cart.entity.Cart;
import com.web.cart.entity.Product;
import com.web.cart.repository.CartRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CartController {
    private CartRepository cartRepository;
    private RestTemplate restTemplate;

    public CartController(CartRepository cartRepository,
                          RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "cartError")
    @RequestMapping(name = "cart", value = "/", method = RequestMethod.GET)
    public Cart cart() {
        return cartRepository.save(new Cart());
    }

    public Cart cartError() {
        return new Cart();
    }

    public Cart cartError(Long id) {
        return new Cart();
    }

    public Cart cartError(Long id, Long product_id) {
        return new Cart();
    }

    public void cartDeleteError(Long id) {}

    @HystrixCommand(fallbackMethod = "cartError")
    @RequestMapping(name = "add_product", value = "/{id}", method = RequestMethod.POST)
    public Cart add(@PathVariable Long id, @RequestBody Long product_id) throws Exception {
        System.out.println(product_id);
        Cart cart = cartRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(id, "Cart"));
        System.out.println(cart);
        System.out.println(product_id);
        Product product = restTemplate.getForObject(
                "http://product-service/" + product_id,
                Product.class
        );
        assert product != null;
        List<Long> products = cart.getProducts();
        products.add(product.getId());
        cart.setProducts(products);
        double total = cart.getTotal();
        total += product.getPrice();
        cart.setTotal(total);
        return cartRepository.save(cart);
    }

    @HystrixCommand(fallbackMethod = "cartError")
    @GetMapping(name = "get_cart", value = "/{id}")
    public Cart get_cart(@PathVariable Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Cart"));
    }

    @HystrixCommand(fallbackMethod = "cartDeleteError")
    @RequestMapping(name = "delete_cart", value = "/{id}", method = RequestMethod.DELETE)
    public void delete_cart(@PathVariable Long id) {
        this.cartRepository.deleteById(id);
    }
}
