package com.web.front.controller;

import com.web.front.entity.*;
import com.web.front.wrapper.ProductList;
import com.web.front.wrapper.ProviderList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HtmlController {

    private RestTemplate restTemplate;
    private PasswordEncoder passwordEncoder;
    private Cart cart = new Cart();
    private String token;

    public HtmlController(RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(name = "index", value = "/")
    public String index(Model model) {
        ProductList response = this.restTemplate.getForObject(
                "http://product-service/",
                ProductList.class
        );
        assert response != null;
        List<Product> products = response.getProductList();
        model.addAttribute("title", "Index");
        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        model.addAttribute("token", token);
        return "index";
    }

    @GetMapping(name = "product", value = "/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product response = this.restTemplate.getForObject(
                "http://router-service/product-service/" + id,
                Product.class
        );
        assert response != null;
        model.addAttribute("title", "Product - " + response.getName());
        model.addAttribute("cart", cart);
        model.addAttribute("product", response);
        model.addAttribute("token", token);
        return "product";
    }

    @GetMapping(name = "product_delete", value = "/product/{id}/delete")
    public String product_delete(@PathVariable Long id, Model model) {
        this.restTemplate.delete("http://router-service/product-service/" + id);
        return "redirect:/";
    }

    @GetMapping(name = "product_add", value = "/product/add")
    public String product_add(Model model) {
        ProviderList response = this.restTemplate.getForObject(
                "http://router-service/product-service/provider/",
                ProviderList.class
        );
        assert response != null;
        List<Provider> providers = response.getProviderList();
        model.addAttribute("title", "Add Product");
        model.addAttribute("cart", cart);
        model.addAttribute("product", new Product());
        model.addAttribute("providers", providers);
        model.addAttribute("token", token);
        return "product_add";
    }

    @PostMapping(name = "product_submit", value = "/product/submit")
    public String product_submit(@ModelAttribute Product product) {
        Product response = this.restTemplate.postForObject(
                "http://router-service/product-service/",
                product,
                Product.class
        );
        return "redirect:/";
    }

    @GetMapping(name = "add_to_cart", value = "/product/{id}/toCart")
    public String add_to_cart(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
        if (this.cart.getId() == null) {
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<Cart> response = restTemplate.exchange(
                    "http://router-service/cart-service/",
                    HttpMethod.GET,
                    entity,
                    Cart.class
            );
            this.cart = response.getBody();
        }
        assert this.cart != null;
        System.out.println(cart);
        HttpEntity<Long> entity = new HttpEntity<Long>(id, httpHeaders);
        ResponseEntity<Cart> responseEntity = restTemplate.exchange(
                "http://router-service/cart-service/" + cart.getId(),
                HttpMethod.POST,
                entity,
                Cart.class
        );
        if (responseEntity != null) {
            this.cart = responseEntity.getBody();
        }
        return "redirect:/";
    }

    @GetMapping(name = "cart", value = "/cart/{id}")
    public String cart(@PathVariable Long id, Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
        HttpEntity<Long> entity = new HttpEntity<Long>(id, httpHeaders);
        ResponseEntity<Cart> responseEntity = restTemplate.exchange(
                "http://router-service/cart-service/" + id,
                HttpMethod.GET,
                entity,
                Cart.class
        );
        Cart response = responseEntity.getBody();
        List<Product> products = new ArrayList<Product>();
        assert response != null;
        if (response.getProducts() != null) {
            for (Long product_id : response.getProducts()) {
                Product product = this.restTemplate.getForObject(
                        "http://product-service/" + product_id,
                        Product.class
                );
                products.add(product);
            }
        }
        System.out.println(products);
        model.addAttribute("cart", response);
        model.addAttribute("products", products);
        model.addAttribute("title", "Cart");
        model.addAttribute("token", token);
        return "cart";
    }

    @GetMapping(name = "order", value = "/cart/{id}/order")
    public String order(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
        HttpEntity<Long> entity = new HttpEntity<Long>(id, httpHeaders);
        ResponseEntity<Cart> responseEntity = restTemplate.exchange(
                "http://router-service/cart-service/" + id,
                HttpMethod.GET,
                entity,
                Cart.class
        );
        Cart response = responseEntity.getBody();
        assert response != null;
        if (response.getId() != null) {
            this.cart = response;
        }
        restTemplate.exchange(
                "http://router-service/cart-service/" + this.cart.getId(),
                HttpMethod.DELETE,
                entity,
                String.class
        );
        //this.restTemplate.delete("http://router-service/cart-service/" + this.cart.getId());
        this.cart = new Cart();
        return "redirect:/";
    }

    @GetMapping(name = "login", value = "/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("cart", cart);
        model.addAttribute("user", new User());
        model.addAttribute("token", token);
        return "login";
    }

    @PostMapping(name = "login_submit", value = "/login/submit")
    public String login_submit(@ModelAttribute User user) {
        Token response = restTemplate.postForObject(
                "http://auth-service/authenticate/",
                user,
                Token.class
        );
        if (response != null) {
            this.token = response.getBody();
        }
        return "redirect:/";
    }

    @GetMapping(name = "register", value = "/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("cart", cart);
        model.addAttribute("user", new User());
        model.addAttribute("token", token);
        return "register";
    }

    @PostMapping(name = "register_submit", value = "/register/submit")
    public String register_submit(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Token response = restTemplate.postForObject(
                "http://auth-service/register/",
                user,
                Token.class
        );
        if (response != null) {
            this.token = response.getBody();
        }
        return "redirect:/";
    }

    @GetMapping(name = "logout", value = "/logout")
    public String logout() {
        this.token = null;
        return "redirect:/";
    }
}
