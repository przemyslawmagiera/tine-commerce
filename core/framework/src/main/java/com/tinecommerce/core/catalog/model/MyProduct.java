package com.tinecommerce.core.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "my_product")
@Getter
@Setter
public class MyProduct extends Product {
    @Column(name= "cecha")
    private String cecha;
}
