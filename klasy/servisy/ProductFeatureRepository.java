package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    List<ProductFeature> findAllByProductId(final Long productId);
}
