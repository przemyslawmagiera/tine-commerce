package com.tinecommerce.core.job;

import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.model.ProductFeature;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.catalog.service.CategoryFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class ClassificationSystemJob {

    @Autowired
    private CategoryFeatureService categoryFeatureService;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 15000)
    @Transactional
    public void updateClassificationSystem() {
        List<? extends Product> products = productRepository.findAllPolimorficEntities();
        products
                .forEach(product -> {
                    List<CategoryFeature> categoryFeatures = categoryFeatureService.getCategoryFeaturesForProduct(product);
                    categoryFeatures.forEach(categoryFeature -> {
                        ProductFeature productFeature = new ProductFeature(product, categoryFeature, "");
                        productFeature.setCode(product.getCode() + ":" + categoryFeature.getCode());
                        product.getProductFeatures().add(productFeature);
                    });
                });
    }
}