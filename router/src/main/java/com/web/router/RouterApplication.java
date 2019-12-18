package com.web.router;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
public class RouterApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RouterApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
