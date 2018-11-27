package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "Category")
@EqualsAndHashCode(exclude = {"products", "parentCategories", "childCategories"}, callSuper = true)
@NoArgsConstructor
public class Category extends AbstractNameableEntity {

    public static final String FIELD_PRODUCTS = "products";

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="product_category",
            joinColumns= @JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="product_id"))
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.Product", mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="category_category",
            joinColumns= @JoinColumn(name="parent_category_id"),
            inverseJoinColumns=@JoinColumn(name="child_category_id"))
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.Category", mappedBy = "parentCategories")
    private Set<Category> childCategories = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="category_category",
            joinColumns= @JoinColumn(name="child_category_id"),
            inverseJoinColumns=@JoinColumn(name="parent_category_id"))
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.Category", mappedBy = "childCategories")
    private Set<Category> parentCategories = new HashSet<>();

    @OneToMany(targetEntity = CategoryFeatureAssignment.class, mappedBy = "category", cascade = CascadeType.ALL)
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.CategoryFeatureAssignment")
    private List<CategoryFeatureAssignment> categoryFeatureAssignments = new ArrayList<>();

    public List<CategoryFeatureAssignment> getCategoryFeatureAssignments() {
        return Collections.unmodifiableList(categoryFeatureAssignments);
    }

    public void addChildCategories(final Category... categories){
        this.childCategories.addAll(Arrays.asList(categories));
    }

    public Set<Category> getChildCategories() {
        return Collections.unmodifiableSet(childCategories);
    }

    public Set<Category> getParentCategories() {
        return Collections.unmodifiableSet(parentCategories);
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    public void addProducts(final Product... products) {
        if (Objects.isNull(this.products)) {
            this.products = new HashSet<>();
        }

        Stream.of(products).forEach(product -> {
            product.getCategories().add(this);
            this.products.add(product);
        });
    }

    @Override
    public String toString() {
        return this.getName() + "- code:" + this.getCode();
    }
}
