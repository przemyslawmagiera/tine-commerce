package com.tinecommerce.admin.panel.controller;


import com.tinecommerce.admin.panel.repository.AdminMenuGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMenuController {

    @Autowired
    private AdminMenuGroupRepository adminMenuGroupRepository;

    @GetMapping("/main-page")
    public String getAdminMainPage(final Model model) {
        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        return "tables";
    }
}
