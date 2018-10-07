package com.tinecommerce.core.extension;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.catalog.model.Product;
import org.apache.solr.common.SolrInputDocument;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtensionUtil {
    public static Class<? extends Product> getCeilingProductClass() {
        Reflections reflections = new Reflections("com.tinecommerce");
        Set<Class<? extends Product>> classes = reflections.getSubTypesOf(Product.class);
        Class<? extends Product> ceilingClass = Product.class;
        int minimum = classes.size();
        for (Class<? extends Product> aClass : classes) {
            if(reflections.getSubTypesOf(aClass).size() < minimum) {
                ceilingClass = aClass;
                minimum = reflections.getSubTypesOf(aClass).size();
            }
        }
        return ceilingClass;
    }

    public static Set<Class<? extends AbstractEntity>> getProductSubclasses(){
        Reflections reflections = new Reflections("com.tinecommerce");
        Set<Class<? extends Product>> classes = reflections.getSubTypesOf(Product.class);
        classes.add(Product.class);
        Set<Class<? extends AbstractEntity>> allClasses = new HashSet<>(classes);
        allClasses.add(AbstractNameableEntity.class);
        return allClasses;
    }
}
