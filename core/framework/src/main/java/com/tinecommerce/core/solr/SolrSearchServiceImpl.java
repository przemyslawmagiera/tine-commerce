package com.tinecommerce.core.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

@Service
public class SolrSearchServiceImpl {
    public SolrDocumentList doSearch() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tenecommerce").build();

        SolrQuery query = new SolrQuery();
        query.setQuery("q:testowy");
        query.setFields("id","name");
        query.setStart(0);
        query.set("defType", "edismax");

        QueryResponse response = client.query(query);
        SolrDocumentList results = response.getResults();
        return results;
    }
}