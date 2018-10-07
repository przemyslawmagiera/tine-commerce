package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryTreeRepository extends JpaRepository<Category, Long> {

    @Override
    @EntityGraph(attributePaths = {"childCategories", "parentCategories"})
    Optional<Category> findById(Long id);

    @EntityGraph(attributePaths = "childCategories")
    List<Category> findAllByProductsContaining(List<Product> products);
}
