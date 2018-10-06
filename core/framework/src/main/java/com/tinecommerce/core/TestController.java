package com.tinecommerce.core;

import com.tinecommerce.core.solr.SolrIndexServiceImpl;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.stream.Stream;

@RestController
public class TestController {

    @Autowired
    SolrIndexServiceImpl solrIndexService;

    @Autowired
    SolrSearchServiceImpl solrSearchService;


    @GetMapping("/")
    public String getResults() throws IOException, SolrServerException {
        solrIndexService.buildIndex();
        return solrSearchService.doSearch();
    }
}
