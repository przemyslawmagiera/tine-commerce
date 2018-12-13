package com.tinecommerce.core.cart;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.customer.model.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class Order extends AbstractEntity {

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(targetEntity = OrderItem.class, mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.cart.OrderItem")
    private Set<OrderItem> orderItems = new HashSet<>();

    @Column(name = "order_status")
    @AdminVisible
    private String orderStatus;

    @Column(name = "submit_date")
    @AdminVisible
    private LocalDate submitDate;

    @Column(name = "order_number")
    @AdminVisible
    private String orderNumber;

    @Column(name = "currency")
    @AdminVisible
    private Currency currency;

    @Column(name = "full_amount")
    @AdminVisible
    private BigDecimal fullAmount;
}
