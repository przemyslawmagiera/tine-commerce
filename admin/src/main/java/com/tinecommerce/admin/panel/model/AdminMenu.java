package com.tinecommerce.admin.panel.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class AdminMenu extends AbstractEntity {

    @Column(name = "NAME", length = 255, nullable = false)
    protected String name;


    @Column(name = "ORDER_NUMBER", nullable = false)
    protected Integer order;
}
