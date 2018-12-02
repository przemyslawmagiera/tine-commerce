package com.tinecommerce.core.solr.impl;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.catalog.model.MediaAsset;
import com.tinecommerce.core.catalog.model.Price;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductFeatureRepository;
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

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SolrIndexServiceImpl implements SolrIndexService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private SearchFieldRepository searchFieldRepository;

    @Resource
    private ProductFeatureRepository productFeatureRepository;

    @Override
    @Transactional
    public void rebuildIndex() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tinecommerce").build();
        List<? extends Product> products = productRepository.findAllPolimorficEntities();
        List<SearchField> fields = searchFieldRepository.findAll();
        clearIndex();
        indexProducts(products, fields, client, Currency.getInstance("PLN"));
        client.commit();
    }

    protected void indexProducts(final List<? extends Product> products, final List<SearchField> fields,
                                 final SolrClient client, final Currency currency) throws IOException, SolrServerException {
        for (final Product product : products) {
            final SolrInputDocument doc = new SolrInputDocument();
            final Set<Class<? extends AbstractEntity>> classes = ExtensionUtil.getProductSubclasses();
            addProductFeatures(product, doc);

            doc.addField("category",
                    product.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
            doc.addField("photos",
                    product.getMediaAssets().stream().map(MediaAsset::getUrl).collect(Collectors.toList()));
            doc.addField("price_d",
                    product.getPrices().stream().filter(price -> price.getCurrency().getCurrencyCode()
                            .equals(currency.getCurrencyCode())).map(Price::getAmount).map(BigDecimal::doubleValue).findFirst().orElse(null));
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
    }

    protected void addProductFeatures (final Product product, final SolrInputDocument inputDocument) {
       productFeatureRepository.findAllByProductId(product.getId())
               .forEach(productFeature ->
                       inputDocument.addField(productFeature.getCategoryFeature().getCode(), productFeature.getValue()));
    }

    private void clearIndex() throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tinecommerce").build();
        client.deleteByQuery("*");
    }
}
