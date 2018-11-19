package com.tinecommerce.admin.panel.repository;

import com.tinecommerce.admin.panel.model.AdminMenuGroup;
import com.tinecommerce.admin.panel.model.AdminMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminMenuItemRepository extends JpaRepository<AdminMenuItem, Long> {
    Optional<AdminMenuItem> findByCode(String code);
    Optional<AdminMenuItem> findByClassName(String className);
}
