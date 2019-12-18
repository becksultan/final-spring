package com.web.product;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableCircuitBreaker
@EnableHystrixDashboard
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

}
