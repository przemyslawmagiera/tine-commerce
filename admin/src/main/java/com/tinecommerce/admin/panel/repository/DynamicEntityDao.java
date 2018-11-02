package com.tinecommerce.admin.panel.repository;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.catalog.model.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public class DynamicEntityDao {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<?> getCeilingClass(String entityClass) throws ClassNotFoundException {
        Reflections reflections = new Reflections("");
        Set<Class<?>> classes = (Set<Class<?>>) reflections.getSubTypesOf(Class.forName(entityClass));
        Class<?> ceilingClass = Class.forName(entityClass);
        int minimum = classes.size();
        for (Class<?> aClass : classes) {
            if(reflections.getSubTypesOf(aClass).size() < minimum) {
                ceilingClass = aClass;
                minimum = reflections.getSubTypesOf(aClass).size();
            }
        }
        return ceilingClass;
    }

    @Transactional
    public List<? extends AbstractEntity> findAllPolimorficEntities(String entityClass) throws ClassNotFoundException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery c = criteriaBuilder.createQuery(getCeilingClass(entityClass));
        Root root = c.from(getCeilingClass(entityClass));
        c.select(root);
        return entityManager.createQuery(c).getResultList();
    }
}
