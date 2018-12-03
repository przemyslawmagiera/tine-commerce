package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "product_feature")
@AllArgsConstructor
@NoArgsConstructor
public class ProductFeature extends AbstractEntity {

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(targetEntity = CategoryFeature.class)
    @JoinColumn(name = "category_feature_id")
    private CategoryFeature categoryFeature;

    @Column(name = "value")
    @AdminVisible
    private String value;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductFeature)) return false;
        if (!super.equals(o)) return false;
        final ProductFeature that = (ProductFeature) o;
        return Objects.equals(product.getId(), that.product.getId()) &&
                Objects.equals(categoryFeature.getId(), that.categoryFeature.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), product.getId(), categoryFeature.getId());
    }
}
