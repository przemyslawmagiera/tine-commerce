package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "Category",
        indexes = {
//                @Index(name = "idx_environment_name", columnList = "name"),
//                @Index(name = "idx_environment_project_id", columnList = "project_id"),
//                @Index(name = "idx_environment_project_version", columnList = "project_version")
        },
        uniqueConstraints = {
                //@UniqueConstraint(columnNames = {"name", "project_version", "project_id"})
        })
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Category extends AbstractEntity {

    public static final String FIELD_PRODUCTS = "products";

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="product_category",
            joinColumns= @JoinColumn(name="product_id"),
            inverseJoinColumns=@JoinColumn(name="category_id"))
    private Set<Product> products = new HashSet<>();

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
}
