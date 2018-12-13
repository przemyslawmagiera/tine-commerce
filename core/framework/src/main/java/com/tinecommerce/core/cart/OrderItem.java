package com.tinecommerce.core.cart;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
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

    @ManyToOne(targetEntity = Order.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "quantity")
    @AdminVisible
    private Long quantity;

    @ManyToOne(targetEntity = ArchivalProduct.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "archival_product_id")
    @AdminVisible
    private ArchivalProduct archivalProduct;
}
