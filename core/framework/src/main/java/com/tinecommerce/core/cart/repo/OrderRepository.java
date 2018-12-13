package com.tinecommerce.core.cart.repo;

import com.tinecommerce.core.cart.Order;
import com.tinecommerce.core.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCustomerAndOrderStatus(Customer customer, String status);

}
