package com.tinecommerce.core.solr;

import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SolrIndexServiceImpl {

    @Autowired
    private ProductRepository productRepository;

    public void buildIndex() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tenecommerce").build();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            SolrInputDocument doc = new SolrInputDocument();
//            doc.addField("cat", product.getCategories());

            doc.addField("id", product.getId());
            doc.addField("name", product.getName());
            client.add(doc);
        }
        client.commit();
    }
}
