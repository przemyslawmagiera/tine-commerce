package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryCustom {
}
