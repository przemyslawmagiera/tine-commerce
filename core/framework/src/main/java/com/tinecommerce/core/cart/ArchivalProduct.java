package com.tinecommerce.core.cart;

import com.tinecommerce.core.AbstractNameableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Archival_Product")
@NoArgsConstructor
@Getter
@Setter
public class ArchivalProduct extends AbstractNameableEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "attr_key")
    @Column(name = "attribute")
    private Map<String, String> attributes = new HashMap<>();

    @Column(name = "price")
    private Double price;

    @Override
    public String toString() {
        return attributes.get("name");
    }
}
