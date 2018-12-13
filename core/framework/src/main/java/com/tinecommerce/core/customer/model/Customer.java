package com.tinecommerce.core.customer.model;

import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.cart.Order;
import com.tinecommerce.core.security.AbstractUserEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

@Entity
@Table(name = "Customer")
@NoArgsConstructor
@EqualsAndHashCode(of = "email", callSuper = false)
@Getter
@Setter
public class Customer extends AbstractUserEntity {
    @NotBlank
    @Size(max = 255)
    @Column(name = "name", length = 255)
    @AdminVisible
    private String name;

    @NotBlank
    @Email
    @Size(max = 254)
    @Column(name = "email", length = 255, unique = true)
    @AdminVisible
    private String email;

    @ManyToMany(mappedBy = "customers", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(targetEntity = Order.class, mappedBy = "customer", cascade = CascadeType.ALL)
    @AdminVisible(tableVisible = false, className = "com.tinecommerce.core.cart.Order")
    private Set<Order> orders = new HashSet<>();

    public Set<Address> getAddresses() {
        return unmodifiableSet(addresses);
    }

    public void addAddress(final Address address) {
        addresses.add(address);
        address.customers.add(this);
    }

    public void removeAddress(final Address address) {
        addresses.remove(address);
        address.customers.remove(this);
    }
}
