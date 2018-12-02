package com.tinecommerce.admin.security.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.model.CategoryFeatureAssignment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admin_permission")
@Getter
@Setter
public class AdminPermission extends AbstractNameableEntity {

    @OneToMany(targetEntity = AdminPermission.class, mappedBy = "adminPermission", cascade = CascadeType.ALL)
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.admin.security.model.AdminPermission", mappedBy = "adminPermission")
    private List<AdminPermission> childPermissions = new ArrayList<>();

    @ManyToOne(targetEntity = AdminPermission.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_permission_id")
    @AdminVisible(tableVisible = false)
    private AdminPermission adminPermission;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="admin_user_permission",
            joinColumns= @JoinColumn(name="admin_user_id"),
            inverseJoinColumns=@JoinColumn(name="admin_permission_id"))
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.admin.security.model.AdminUser", mappedBy = "adminPermissions")
    private Set<AdminUser> adminUsers = new HashSet<>();

    @Column(name = "class_name")
    @AdminVisible
    private String className;

}
