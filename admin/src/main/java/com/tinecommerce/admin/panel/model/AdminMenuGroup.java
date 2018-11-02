package com.tinecommerce.admin.panel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "ADMIN_MENU_GROUP")
public class AdminMenuGroup extends AdminMenu {
    @OneToMany(mappedBy = "adminMenuGroup", fetch = FetchType.EAGER)
    private Set<AdminMenuItem> adminMenuItems;

    public Set<AdminMenuItem> getAdminMenuItems() {
        return adminMenuItems;
    }

    public void setAdminMenuItems(Set<AdminMenuItem> adminMenuItems) {
        this.adminMenuItems = adminMenuItems;
    }
}
