package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<? extends Product> findByField(String fieldName, Object value);

    List<? extends Product> findAllPolimorficEntities();
}
