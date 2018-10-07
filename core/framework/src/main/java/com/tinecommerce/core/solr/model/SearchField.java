package com.tinecommerce.core.solr.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "search_field")
@Getter
@Setter
public class SearchField extends AbstractEntity {

    @Column(name = "field_name")
    private String name;

    @Column(name = "searchable")
    private Boolean searchable;

    @Column(name = "is_facet")
    private Boolean isFacet;

}
