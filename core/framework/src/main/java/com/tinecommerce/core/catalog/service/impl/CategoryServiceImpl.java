package com.tinecommerce.core.catalog.service.impl;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.service.CategoryService;
import com.tinecommerce.core.exception.CircularEntityConnectionException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);

    public List<Category> getAllChildCategories(final Category category) {
        final List<Category> result = new ArrayList<>();
        getAllChildCategories(category, result);
        return result;
    }

    private void getAllChildCategories(final Category category, final List<Category> result) {
        category.getChildCategories().forEach(child -> this.getAllChildCategories(child, result));
        result.add(category);
    }

    public List<Category> getAllParentCategories(final Category category) {
        final List<Category> result = new ArrayList<>();
        getAllParentCategories(category, result);
        return result;
    }

    private void getAllParentCategories(final Category category, final List<Category> result) {
        category.getParentCategories().forEach(parent -> this.getAllChildCategories(parent, result));
        result.add(category);
    }

    public Boolean canAddChildToCategory(final Category category, final Category child) {
        final List<Category> childCategories = new ArrayList<>();
        getAllChildCategories(category,childCategories);
        return childCategories.contains(child);
    }

    @Transactional
    public void addChildToCategory(final Category category, final Category child) throws CircularEntityConnectionException {
        if(canAddChildToCategory(category, child)) {
            category.addChildCategories(child);
        }
        else {
            final String message = "Cannot add " + child.toString() + " as a child of " + category.toString() + " circle detected!";
            LOGGER.error(message);
            throw new CircularEntityConnectionException(message);
        }
    }

}
