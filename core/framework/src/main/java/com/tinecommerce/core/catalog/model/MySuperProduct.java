package com.tinecommerce.core.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "my_super_product")
@Getter
@Setter
public class MySuperProduct extends MyProduct {
    @Column(name= "cecha1")
    private String cecha1;
}
