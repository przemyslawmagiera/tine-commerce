package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractNamableEntity;
import com.tinecommerce.core.catalog.enumeration.CategoryFeatureType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Category_feature")
public class CategoryFeature extends AbstractNamableEntity
{

   @Enumerated(EnumType.STRING)
   @Column(name = "TYPE", length = 64, nullable = false)
   protected CategoryFeatureType type;

   @OneToMany(targetEntity = CategoryFeatureAssignment.class, mappedBy = "categoryFeature", cascade = CascadeType.ALL,
           orphanRemoval = true)
   protected Set<CategoryFeatureAssignment> categoryFeatureAssignments;

   @OneToMany(targetEntity = CategoryFeatureValue.class, mappedBy = "categoryFeature", fetch = FetchType.LAZY,
           cascade = CascadeType.ALL, orphanRemoval = true)
   protected Set<CategoryFeatureValue> categoryFeatureValues;

   @Column(name = "searchable", nullable = false)
   protected Boolean searchable;

   @Column(name = "facet", nullable = false)
   protected Boolean facet;
}
