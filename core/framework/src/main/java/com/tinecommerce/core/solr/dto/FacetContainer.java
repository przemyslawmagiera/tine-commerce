package com.tinecommerce.core.solr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FacetContainer {
    private String code;
    private List<FacetDTO> facetDTOS = new ArrayList<>();
}
