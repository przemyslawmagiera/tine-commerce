package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface CategoryFeatureRepository extends JpaRepository<CategoryFeature, Long> {

    Set<CategoryFeature> findAllByIsFacet(Boolean isFacet);

    Set<CategoryFeature> findAllBySearchable(Boolean b);
}
