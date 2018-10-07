package com.tinecommerce.core.catalog.service.impl;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.model.CategoryFeatureAssignment;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.CategoryTreeRepository;
import com.tinecommerce.core.catalog.service.CategoryFeatureService;
import com.tinecommerce.core.catalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryFeatureServiceImpl implements CategoryFeatureService {

    @Autowired
    private CategoryTreeRepository categoryTreeRepository;

    @Autowired
    private CategoryService categoryService;

    public List<CategoryFeature> getCategoryFeaturesForProduct(final Product product) {
        return categoryTreeRepository
                .findAllByProductsContaining(Collections.singletonList(product))
                .stream()
                .map(category -> categoryService.getAllParentCategories(category))
                .flatMap(Collection::stream)
                .map(Category::getCategoryFeatureAssignments)
                .flatMap(Collection::stream)
                .map(CategoryFeatureAssignment::getCategoryFeature)
                .collect(Collectors.toList());
    }
}
