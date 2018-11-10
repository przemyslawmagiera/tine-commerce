package com.tinecommerce.core.solr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceRange {
    String to;
    String from;
    Integer count;
}
