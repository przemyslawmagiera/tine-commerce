package com.tinecommerce.core.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Order_item")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @ManyToOne(targetEntity = Order.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<Order> orders;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(targetEntity = ArchivalProduct.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "archival_product_id")
    private ArchivalProduct archivalProduct;
}
