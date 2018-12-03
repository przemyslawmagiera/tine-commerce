package com.tinecommerce.core.web.component;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import com.tinecommerce.core.solr.dto.ProductDTO;
import com.tinecommerce.core.solr.dto.SearchResultDTO;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductDetailCmp {

    @Autowired
    SolrSearchServiceImpl solrSearchService;

    public ProductDTO getProductDetailsByCode(String code) throws IOException, SolrServerException {
        Map<String, String> params = new HashMap<>();
        params.put(AbstractEntity.FIELD_CODE+"_t", code);
        params.put("query", "*");
        SearchResultDTO searchResultDTO = solrSearchService.doSearch(params);
        return searchResultDTO.getProductDTOS().stream().findFirst().orElse(new ProductDTO());

    }

}
