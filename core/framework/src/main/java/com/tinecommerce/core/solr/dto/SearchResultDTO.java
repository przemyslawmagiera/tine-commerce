package com.tinecommerce.core.solr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResultDTO {
    private List<ProductDTO> productDTOS = new ArrayList<>();
    private List<FacetContainer> facetContainers = new ArrayList<>();
}
