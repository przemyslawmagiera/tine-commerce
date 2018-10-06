package com.tinecommerce.core.catalog.model;


import com.tinecommerce.core.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "Product",
        indexes = {
//                @Index(name = "idx_environment_name", columnList = "name"),
//                @Index(name = "idx_environment_project_id", columnList = "project_id"),
//                @Index(name = "idx_environment_project_version", columnList = "project_version")
        },
        uniqueConstraints = {
//                @UniqueConstraint(columnNames = {"name", "project_version", "project_id"})
        })
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Product extends AbstractEntity {

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Price> prices;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = Category.FIELD_PRODUCTS, cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public void addCategories(final Category... categories) {
        if (Objects.isNull(this.categories)) {
            this.categories = new HashSet<>();
        }

        Stream.of(categories).forEach(category -> {
            category.getProducts().add(this);
            this.categories.add(category);
        });
    }

    public Set<Category> getPrices() {
        return Collections.unmodifiableSet(categories);
    }
}
