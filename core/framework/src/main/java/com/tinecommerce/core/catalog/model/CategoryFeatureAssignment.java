package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Index;

import javax.persistence.*;

@Entity
@Table(name = "category_feature_assignment")
@Getter
@Setter
public class CategoryFeatureAssignment extends AbstractEntity
{

   @ManyToOne(targetEntity = CategoryFeature.class, cascade = CascadeType.ALL)
   @JoinColumn(name = "category_feature_id")
   @AdminVisible
   private CategoryFeature categoryFeature;

   @ManyToOne(targetEntity = Category.class, cascade = CascadeType.REFRESH)
   @JoinColumn(name = "category_id")
   @AdminVisible
   private Category category;
}
