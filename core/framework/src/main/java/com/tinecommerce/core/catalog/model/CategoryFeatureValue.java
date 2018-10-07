package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "category_feature_value")
@Getter
@Setter
public class CategoryFeatureValue extends AbstractEntity
{
   @Column(name = "VALUE", nullable = false)
   protected String value;

   @ManyToOne(targetEntity = CategoryFeature.class, optional = false, cascade = CascadeType.ALL)
   @JoinColumn(name = "category_feature_id")
   protected CategoryFeature categoryFeature;

}
