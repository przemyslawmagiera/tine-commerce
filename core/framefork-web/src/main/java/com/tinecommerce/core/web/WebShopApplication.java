package com.tinecommerce.core.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan({"com.tinecommerce.core", "com.tinecommerce.core.web"})
public class WebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopApplication.class, args);
    }
}
