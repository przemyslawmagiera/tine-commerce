package com.tinecommerce.core.catalog.model;


import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.AdminVisible;
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
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Product extends AbstractNameableEntity {

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.Price")
    private Set<Price> prices;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.MediaAsset")
    private Set<MediaAsset> mediaAssets;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.ProductFeature")
    private Set<ProductFeature> productFeatures = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = Category.FIELD_PRODUCTS, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.catalog.model.Category", mappedBy = "products")
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

    public Set<Price> getPrices() {
        return Collections.unmodifiableSet(prices);
    }

    @Override
    public String toString() {
        return this.getName() + " - " + this.getCode();
    }
}
