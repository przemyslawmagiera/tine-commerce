package com.tinecommerce.core.customer.repository;

import com.tinecommerce.core.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsernameOrEmail(String username, String email);
}
