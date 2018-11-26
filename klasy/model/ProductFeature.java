package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product_feature")
public class ProductFeature extends AbstractEntity {

    @ManyToOne(targetEntity = Product.class, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(targetEntity = CategoryFeature.class, optional = false)
    @JoinColumn(name = "category_feature_id")
    private CategoryFeature categoryFeature;

    @Column(name = "value")
    private String value;
}
