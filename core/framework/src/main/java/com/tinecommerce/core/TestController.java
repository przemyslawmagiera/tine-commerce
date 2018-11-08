package com.tinecommerce.core;

import com.tinecommerce.core.catalog.repository.CategoryRepository;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.catalog.service.CategoryFeatureService;
import com.tinecommerce.core.catalog.service.impl.CategoryServiceImpl;
import com.tinecommerce.core.solr.SolrIndexService;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import com.tinecommerce.core.solr.dto.SearchResultDTO;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class TestController {

    @Resource
    SolrIndexService solrIndexService;

    @Resource
    SolrSearchServiceImpl solrSearchService;

    @Resource
    CategoryRepository categoryRepository;

    @Resource
    CategoryServiceImpl categoryService;

    @Resource
    CategoryFeatureService categoryFeatureService;

    @Resource
    ProductRepository productRepository;

    @GetMapping("/search")
    @ResponseBody
    public SearchResultDTO getResults(@RequestParam String query) throws IOException, SolrServerException {
//        solrIndexService.buildIndex();
        return  solrSearchService.doSearch(query, "price_d asc");
    }
}
