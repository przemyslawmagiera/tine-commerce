package com.tinecommerce.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/customer/cart/show")
    public String showCart(Model model){
        return "cart";
    }

}
