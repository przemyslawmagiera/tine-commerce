package com.tinecommerce.core.solr.repository;

import com.tinecommerce.core.solr.model.SearchField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchFieldRepository extends JpaRepository<SearchField, Long> {
}
