package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.catalog.enumeration.CategoryFeatureType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Category_feature")
@Getter
@Setter
public class CategoryFeature extends AbstractNameableEntity
{

   @Enumerated(EnumType.STRING)
   @Column(name = "TYPE", length = 64, nullable = false)
   private CategoryFeatureType type;

   @OneToMany(targetEntity = CategoryFeatureAssignment.class, mappedBy = "categoryFeature", cascade = CascadeType.ALL,
           orphanRemoval = true)
   private Set<CategoryFeatureAssignment> categoryFeatureAssignments;

   @OneToMany(targetEntity = CategoryFeatureValue.class, mappedBy = "categoryFeature", fetch = FetchType.LAZY,
           cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<CategoryFeatureValue> categoryFeatureValues;

   @Column(name = "searchable", nullable = false)
   private Boolean searchable;

   @Column(name = "facet", nullable = false)
   private Boolean facet;
}
