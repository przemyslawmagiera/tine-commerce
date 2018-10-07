package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByProductsContaining(List<Product> products);
}
