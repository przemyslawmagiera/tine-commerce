package com.tinecommerce.core.extension;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.catalog.model.Product;
import org.apache.solr.common.SolrInputDocument;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Set<Class<?>> getSubclassesOf(final String className) throws ClassNotFoundException {
        Reflections reflections = new Reflections("");
        Class<?> aClass = Class.forName(className);
        Set<Class<?>> classes = new HashSet<>();
        if(AbstractNameableEntity.class.isAssignableFrom(aClass))
        {
            classes.add(AbstractNameableEntity.class);
        }
        if(AbstractEntity.class.isAssignableFrom(aClass))
        {
            classes.add(AbstractEntity.class);
        }
        classes.addAll((Set<Class<?>>) reflections.getSubTypesOf(aClass));
        classes.add(aClass);
        return classes;
    }

    public static Set<Field> getPolymorphicFielsdOf(final String className) throws ClassNotFoundException {
        Set<Class<?>> classes = getSubclassesOf(className);
        return classes.stream()
                .map(Class::getDeclaredFields)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public static Field findFieldInPolimorficClass(final String className, final String fieldName) throws ClassNotFoundException {
        Set<Field> fields = getPolymorphicFielsdOf(className);
        for (Field field : fields) {
            for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                try {
                    Field classField = cls.getDeclaredField(field.getName());
                    if (classField.getName().equals(fieldName))
                    {
                        return classField;
                    }
                } catch (NoSuchFieldException nsfe) {
                }
            }
        }
        return null;
    }
}
