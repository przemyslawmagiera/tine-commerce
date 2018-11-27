package com.tinecommerce.admin.panel.repository;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.CategoryRepository;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<AbstractEntity> findAllPolimorficEntities(String entityClass) throws ClassNotFoundException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery c = criteriaBuilder.createQuery(getCeilingClass(entityClass));
        Root root = c.from(getCeilingClass(entityClass));
        c.select(root);
        return entityManager.createQuery(c).getResultList();
    }

    public AbstractEntity findAllPolimorficEntityWithId(String className, Long id) throws ClassNotFoundException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery c = criteriaBuilder.createQuery(getCeilingClass(className));
        Root root = c.from(getCeilingClass(className));
        c.select(root);
        c.where(criteriaBuilder.equal(root.get(AbstractEntity.FIELD_ID), id));
        return (AbstractEntity) entityManager.createQuery(c).getSingleResult();
    }

    @Autowired
    private CategoryRepository categoryRepository;

    public List<AbstractEntity> findAllPolimorficEntitiesWithForeignKey(String className, String foreignKeyField, Long foreignKey) throws ClassNotFoundException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery c = criteriaBuilder.createQuery(getCeilingClass(className));
        Root root = c.from(getCeilingClass(className));
        c.select(root);
        c.where(criteriaBuilder.equal(root.get(foreignKeyField).get(AbstractEntity.FIELD_ID), foreignKey));
        return (List<AbstractEntity>) entityManager.createQuery(c).getResultList();
    }

    public List<AbstractEntity> findAllPolimorficEntitiesWithManyToManyRelation(String className, String manyToManyField, Long foreignKey) throws ClassNotFoundException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery c = criteriaBuilder.createQuery(getCeilingClass(className));
        Root root = c.from(getCeilingClass(className));
        c.select(root);
        c.where(root.join(manyToManyField).get(AbstractEntity.FIELD_ID).in(foreignKey));
        return (List<AbstractEntity>) entityManager.createQuery(c).getResultList();
    }

    @Transactional
    public void save(AbstractEntity abstractEntity) {
        entityManager.persist(abstractEntity);
    }

    @Transactional
    public void delete(AbstractEntity abstractEntity) {
        entityManager.remove(abstractEntity);
    }
}
