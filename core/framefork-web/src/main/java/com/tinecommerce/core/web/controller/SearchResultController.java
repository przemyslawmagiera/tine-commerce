package com.tinecommerce.core.web.controller;

import com.tinecommerce.core.solr.SolrIndexService;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import com.tinecommerce.core.solr.dto.SearchResultDTO;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
public class SearchResultController {

    @Autowired
    protected SolrSearchServiceImpl solrSearchService;

    @Autowired
    protected SolrIndexService solrIndexService;

    @GetMapping("/search")
    @ResponseBody
    public SearchResultDTO getResults(@RequestParam String query, @RequestParam(required = false) String sort) throws IOException, SolrServerException {
        solrIndexService.rebuildIndex();
        return  solrSearchService.doSearch(query, sort);
    }
    @GetMapping("/search-view")
    public String getSearchView(final Model model) throws IOException, SolrServerException {
        return "index";
    }
}
