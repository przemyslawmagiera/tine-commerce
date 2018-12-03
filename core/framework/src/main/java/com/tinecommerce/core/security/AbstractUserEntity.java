package com.tinecommerce.core.security;

import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class AbstractUserEntity extends AbstractEntity {
    @Column(name = "username")
    @AdminVisible
    private String username;

    @Column(name = "password")
    @AdminVisible(tableVisible = false)
    private String password;

    @Column(name = "role")
    private String role;

}