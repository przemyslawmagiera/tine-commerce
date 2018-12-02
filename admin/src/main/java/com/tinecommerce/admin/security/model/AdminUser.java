package com.tinecommerce.admin.security.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "admin_user")
public class AdminUser extends AbstractEntity {
    @Column(name = "username")
    @AdminVisible
    private String username;

    @Column(name = "password")
    @AdminVisible(tableVisible = false)
    private String password;

    @Column(name = "role")
    private String role;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "adminUsers")
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.admin.security.model.AdminPermission", mappedBy = "adminUsers")
    private Set<AdminPermission> adminPermissions = new HashSet<>();
}
