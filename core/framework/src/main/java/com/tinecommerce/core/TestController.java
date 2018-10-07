package com.tinecommerce.core;

import com.tinecommerce.core.catalog.repository.CategoryRepository;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.catalog.service.CategoryFeatureService;
import com.tinecommerce.core.catalog.service.impl.CategoryServiceImpl;
import com.tinecommerce.core.solr.SolrIndexServiceImpl;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    SolrIndexServiceImpl solrIndexService;

    @Autowired
    SolrSearchServiceImpl solrSearchService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    CategoryFeatureService categoryFeatureService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/")
    public String getResults() throws IOException, SolrServerException {
//        solrIndexService.buildIndex();
//        productRepository.findByField("cecha", "chujowy");
        productRepository.findByField("cecha1","dupson");
        categoryFeatureService.getCategoryFeaturesForProduct(productRepository.getOne(1L));
        return null;
    }
}
