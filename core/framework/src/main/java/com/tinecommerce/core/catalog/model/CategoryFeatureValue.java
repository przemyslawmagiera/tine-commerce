package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "category_feature_value")
public class CategoryFeatureValue extends AbstractEntity
{
   @Column(name = "VALUE", nullable = false)
   protected String value;

   @ManyToOne(targetEntity = CategoryFeature.class, optional = false, cascade = CascadeType.ALL)
   @JoinColumn(name = "category_feature_id")
   protected CategoryFeature classificationAttribute;

}
