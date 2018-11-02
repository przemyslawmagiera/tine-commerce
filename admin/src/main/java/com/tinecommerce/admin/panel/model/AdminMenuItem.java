package com.tinecommerce.admin.panel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ADMIN_MENU_ITEM")
public class AdminMenuItem extends AdminMenu {
    @Column(name = "CLASS_NAME", nullable = false)
    private String className;

    @Column(name = "FRIENDLY_NAME", nullable = false)
    private String friendlyName;

    @ManyToOne(targetEntity = AdminMenuGroup.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private AdminMenuGroup adminMenuGroup;
}
