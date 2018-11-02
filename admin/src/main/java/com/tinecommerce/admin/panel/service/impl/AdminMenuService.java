package com.tinecommerce.admin.panel.service.impl;

import com.tinecommerce.admin.panel.model.AdminMenuGroup;
import com.tinecommerce.admin.panel.repository.AdminMenuGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdminMenuService {

    @Autowired
    private AdminMenuGroupRepository adminMenuGroupRepository;

    @Transactional
    public List<AdminMenuGroup> getMenuGroups() {
        List<AdminMenuGroup> adminMenuGroups = adminMenuGroupRepository.findAll();
        return null;
    }
}
