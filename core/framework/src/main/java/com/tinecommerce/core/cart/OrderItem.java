package com.tinecommerce.core.cart;

import com.tinecommerce.core.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Order_item")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem extends AbstractEntity {

    @ManyToOne(targetEntity = Order.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(targetEntity = ArchivalProduct.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "archival_product_id")
    private ArchivalProduct archivalProduct;
}
