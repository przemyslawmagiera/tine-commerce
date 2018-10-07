package com.tinecommerce.core.solr.impl;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.extension.ExtensionUtil;
import com.tinecommerce.core.solr.SolrIndexService;
import com.tinecommerce.core.solr.model.SearchField;
import com.tinecommerce.core.solr.repository.SearchFieldRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SolrIndexServiceImpl implements SolrIndexService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SearchFieldRepository searchFieldRepository;

    @Override
    public void rebuildIndex() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tenecommerce").build();
        List<? extends Product> products = productRepository.findAllPolimorficEntities();
        List<SearchField> fields = searchFieldRepository.findAll();
        clearIndex();
        for (Product product : products) {
            SolrInputDocument doc = new SolrInputDocument();
            Set<Class<? extends AbstractEntity>> classes = ExtensionUtil.getProductSubclasses();
            for (SearchField field : fields) {
                for (Class<? extends AbstractEntity> cls : classes) {
                    try {
                        Field classField = cls.getDeclaredField(field.getName());
                        classField.setAccessible(true);
                        doc.addField(field.getName(), classField.get(product));
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
            client.add(doc);
        }
        client.commit();
    }

    private void clearIndex() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tenecommerce").build();
        client.deleteByQuery("*");
    }
}
