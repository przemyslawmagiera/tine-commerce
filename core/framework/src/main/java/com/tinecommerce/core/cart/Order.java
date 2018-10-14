package com.tinecommerce.core.cart;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.customer.model.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"order\"")
@NoArgsConstructor
@Getter
@Setter
public class Order extends AbstractEntity {

    @ManyToOne(targetEntity = Customer.class, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(targetEntity = OrderItem.class, mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.NEW;

    @Column(name = "submit_date")
    private LocalDate submitDate;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "full_amount")
    private BigInteger fullAmount;
}
