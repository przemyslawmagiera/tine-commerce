package com.tinecommerce.core.customer.model;

import com.tinecommerce.core.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "address", indexes = {
        @Index(name = "person_code_idx", columnList = "code", unique = false),
        @Index(name = "person_number_idx", columnList = "number", unique = false),
        @Index(name = "person_flat_number_idx", columnList = "flat_number", unique = false)
})
@NoArgsConstructor
@Getter
@Setter
public class Address extends AbstractEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "street", length = 255)
    private String street;

    @NotBlank
    @Size(max = 15)
    @Column(name = "number", length = 15)
    private String number;

    @Size(max = 15)
    @Column(name = "flat_number", nullable = true, length = 15)
    private String flatNumber;

    @NotBlank
    @Size(max = 255)
    @Column(name = "code", length = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;

    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "address_person",
            inverseJoinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")},
            joinColumns = {@JoinColumn(name = "address_id", referencedColumnName = "id")}
    )
    List<Customer> customers = new ArrayList<>();

    public List<Customer> getPeople() {
        return Collections.unmodifiableList(customers);
    }
}
