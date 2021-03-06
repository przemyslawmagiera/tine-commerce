package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "category_feature_value")
@Getter
@Setter
public class CategoryFeatureValue extends AbstractEntity
{
   @Column(name = "VALUE")
   @AdminVisible
   protected String value;

   @ManyToOne(targetEntity = CategoryFeature.class, optional = true, cascade = CascadeType.ALL)
   @JoinColumn(name = "category_feature_id")
   @AdminVisible
   protected CategoryFeature categoryFeature;

}
