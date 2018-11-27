package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "Price",
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
@AllArgsConstructor
public class Price extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "currency")
    @AdminVisible
    private Currency currency;

    @Column(name = "amount")
    @AdminVisible
    private BigDecimal amount;
}
