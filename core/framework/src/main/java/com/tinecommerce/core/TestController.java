package com.tinecommerce.core;

import com.tinecommerce.core.catalog.repository.CategoryTreeRepository;
import com.tinecommerce.core.catalog.service.impl.CategoryServiceImpl;
import com.tinecommerce.core.solr.SolrIndexServiceImpl;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class TestController {

    @Autowired
    SolrIndexServiceImpl solrIndexService;

    @Autowired
    SolrSearchServiceImpl solrSearchService;

    @Autowired
    CategoryTreeRepository categoryTreeRepository;

    @Autowired
    CategoryServiceImpl categoryService;


    @GetMapping("/")
    public String getResults() throws IOException, SolrServerException {
//        solrIndexService.buildIndex();
        categoryTreeRepository.findAll();
        categoryService.getAllChildCategories(categoryTreeRepository.getById(1L).get(), new ArrayList<>());
        return null;
    }
}
