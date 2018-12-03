package com.tinecommerce.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/cart")
public class CartController {

    @GetMapping(name = "/show")
    public String showCart(Model model){
        return "car-temp";
    }
}
