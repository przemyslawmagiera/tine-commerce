package com.tinecommerce.core.web.controller;

import com.tinecommerce.core.solr.SolrIndexService;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import com.tinecommerce.core.solr.dto.SearchResultDTO;
import com.tinecommerce.core.web.component.ProductDetailCmp;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
public class SearchResultController {

    @Autowired
    protected SolrSearchServiceImpl solrSearchService;

    @Autowired
    protected SolrIndexService solrIndexService;

    @Autowired
    protected ProductDetailCmp productDetailCmp;

    @GetMapping("/search")
    public String getResults(final Model model, @RequestParam String query, @RequestParam(required = false) String sort,
                             @RequestParam(required = false) String priceFilter, @RequestParam Map<String,String> filterParams)
            throws IOException, SolrServerException {
        solrIndexService.rebuildIndex();
        SearchResultDTO searchResultDTO = solrSearchService.doSearch(filterParams);
        model.addAttribute("searchResult", searchResultDTO);
        return "index";
    }

    @GetMapping("/productDetail/{code}")
    public String getPD(final Model model, @PathVariable(name = "code") String code)
            throws IOException, SolrServerException {
        model.addAttribute("product", productDetailCmp.getProductDetailsByCode(code));
        return "productDetail";
    }
}
