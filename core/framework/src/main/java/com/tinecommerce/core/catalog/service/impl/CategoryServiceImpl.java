package com.tinecommerce.core.catalog.service.impl;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.repository.CategoryRepository;
import com.tinecommerce.core.catalog.service.CategoryService;
import com.tinecommerce.core.exception.CircularEntityConnectionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public List<Category> getAllChildCategories(Category category) {
        final List<Category> result = new ArrayList<>();
        category = categoryRepository.getOne(category.getId());
        getAllChildCategories(category, result);
        return result;
    }

    private void getAllChildCategories(final Category category, final List<Category> result) {
        category.getChildCategories().forEach(child -> this.getAllChildCategories(child, result));
        result.add(category);
    }

    @Transactional
    public List<Category> getAllParentCategories(Category category) {
        final List<Category> result = new ArrayList<>();
        category = categoryRepository.getOne(category.getId());
        getAllParentCategories(category, result);
        return result;
    }

    private void getAllParentCategories(final Category category, final List<Category> result) {
        category.getParentCategories().forEach(parent -> this.getAllParentCategories(parent, result));
        result.add(category);
    }

    public Boolean canAddChildToCategory(final Category category, final Category child) {
        return getAllChildCategories(category).contains(child);
    }

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
