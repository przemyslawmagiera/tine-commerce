package com.tinecommerce.admin.panel.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ADMIN_MENU_ITEM")
public class AdminMenuItem extends AbstractEntity {
    @Column(name = "NAME", length = 255, nullable = false)
    @AdminVisible
    protected String name;

    @Column(name = "ORDER_NUMBER", nullable = false)
    protected Integer order;

    @Column(name = "CLASS_NAME")
    @AdminVisible
    private String className;

    @Column(name = "FRIENDLY_NAME")
    @AdminVisible
    private String friendlyName;

    @ManyToOne(targetEntity = AdminMenuGroup.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private AdminMenuGroup adminMenuGroup;
}
