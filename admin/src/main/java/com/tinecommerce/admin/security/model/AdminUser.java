package com.tinecommerce.admin.security.model;

import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.security.AbstractUserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "admin_user")
public class AdminUser extends AbstractUserEntity {

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "adminUsers")
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.admin.security.model.AdminPermission", mappedBy = "adminUsers")
    private Set<AdminPermission> adminPermissions = new HashSet<>();
}
