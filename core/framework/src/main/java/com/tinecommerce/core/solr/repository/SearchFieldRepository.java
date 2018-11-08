package com.tinecommerce.core.solr.repository;

import com.tinecommerce.core.solr.model.SearchField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SearchFieldRepository extends JpaRepository<SearchField, Long> {
    Set<SearchField> findAllBySearchable(boolean searchable);
    Set<SearchField> findAllByIsFacet(Boolean isFacet);
}
