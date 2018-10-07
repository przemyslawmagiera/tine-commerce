package com.tinecommerce.core.solr;

import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.extension.ExtensionUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SolrIndexServiceImpl {

    @Autowired
    private ProductRepository productRepository;

    public void buildIndex() throws IOException, SolrServerException, NoSuchFieldException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tenecommerce").build();
        List<? extends Product> products = productRepository.findAllPolimorficEntities();
        List<String> fields = new ArrayList<>();
        client.deleteByQuery("*");
        fields.add("name");
        fields.add("cecha");
        for (Product product : products) {
            SolrInputDocument doc = new SolrInputDocument();
            Reflections reflections = new Reflections("com.tinecommerce");
            Set<Class<? extends Product>> classes = reflections.getSubTypesOf(Product.class);
            classes.add(Product.class);
            for (String field : fields) {
                for (Class<? extends Product> cls : classes) {
                    try {
                        Field classField = cls.getDeclaredField(field);
                        classField.setAccessible(true);
                        doc.addField(field, classField.get(product));
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
            client.add(doc);
        }
        client.commit();
    }
}
