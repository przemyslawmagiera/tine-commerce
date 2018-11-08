package com.tinecommerce.core.solr.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProductDTO {
    private Map<String, Object> attributes = new HashMap<>();
}
