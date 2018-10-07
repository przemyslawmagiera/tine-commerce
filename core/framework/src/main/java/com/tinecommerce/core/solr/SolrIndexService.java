package com.tinecommerce.core.solr;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

public interface SolrIndexService {
    void rebuildIndex() throws IOException, SolrServerException;
}
