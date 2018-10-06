package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import org.hibernate.annotations.Index;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "category_feature_assignment")
public class CategoryFeatureAssignment extends AbstractEntity
{

   @ManyToOne(targetEntity = CategoryFeature.class, optional = false, cascade = CascadeType.ALL)
   @JoinColumn(name = "category_feature_id")
   private CategoryFeature categoryFeature;

   @ManyToOne(targetEntity = Category.class, optional = false, cascade = CascadeType.REFRESH)
   @JoinColumn(name = "category_id")
   private Category category;
}
