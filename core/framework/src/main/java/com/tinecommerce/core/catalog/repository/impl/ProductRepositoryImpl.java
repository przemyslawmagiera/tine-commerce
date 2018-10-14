package com.tinecommerce.core.catalog.repository.impl;

import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductRepositoryCustom;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final Logger LOGGER = Logger.getLogger(ProductRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private Class<? extends Product> getCeilingClass(){
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

    @Override
    public List<? extends Product> findByField(String fieldName, Object value)
    {
        Session session = entityManager.unwrap(Session.class);
        Criteria c = session.createCriteria(getCeilingClass());
        c.add(Restrictions.eq(fieldName, value));
        return c.list();
    }

    @Override
    public List<? extends Product> findAllPolimorficEntities() {
        Session session = entityManager.unwrap(Session.class);
        Criteria c = session.createCriteria(getCeilingClass());
        return c.list();
    }
}
