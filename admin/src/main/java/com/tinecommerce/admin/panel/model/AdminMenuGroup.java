package com.tinecommerce.admin.panel.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "ADMIN_MENU_GROUP")
public class AdminMenuGroup extends AbstractEntity {
    @Column(name = "NAME", length = 255, nullable = false)
    @AdminVisible
    protected String name;

    @Column(name = "ORDER_NUMBER", nullable = false)
    protected Integer order;

    @OneToMany(mappedBy = "adminMenuGroup", fetch = FetchType.EAGER)
    @AdminVisible(className = "com.tinecommerce.admin.panel.model.AdminMenuItem", tableVisible = false)
    private Set<AdminMenuItem> adminMenuItems;

    public Set<AdminMenuItem> getAdminMenuItems() {
        return adminMenuItems;
    }

    public void setAdminMenuItems(Set<AdminMenuItem> adminMenuItems) {
        this.adminMenuItems = adminMenuItems;
    }
}
