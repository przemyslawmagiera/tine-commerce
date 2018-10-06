package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;

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
@AllArgsConstructor
public class Price extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private Currency currency;

    @Column(name = "name", nullable = false)
    private BigDecimal amount;
}
