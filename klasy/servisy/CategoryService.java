package com.tinecommerce.core.catalog.service;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.exception.CircularEntityConnectionException;

import java.util.List;

public interface CategoryService {
    List<Category> getAllChildCategories(final Category category);

    List<Category> getAllParentCategories(final Category category);

    Boolean canAddChildToCategory(final Category category, final Category child);

    void addChildToCategory(final Category category, final Category child) throws CircularEntityConnectionException;
}
