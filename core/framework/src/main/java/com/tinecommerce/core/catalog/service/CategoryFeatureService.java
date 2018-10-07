package com.tinecommerce.core.catalog.service;

import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.model.Product;

import java.util.List;

public interface CategoryFeatureService {
    List<CategoryFeature> getCategoryFeaturesForProduct(Product product);
}
