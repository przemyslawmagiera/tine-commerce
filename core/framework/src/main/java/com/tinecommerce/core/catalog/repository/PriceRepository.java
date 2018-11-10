package com.tinecommerce.core.catalog.repository;

import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.Price;
import com.tinecommerce.core.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
