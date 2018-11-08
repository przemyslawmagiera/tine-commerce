package com.tinecommerce.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of = "code")
public abstract class AbstractEntity implements Serializable {

    public static final String FIELD_CODE = "code";

    private static final long serialVersionUID = -2968157318009412565L;

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "increment")
    @AdminVisible(order = 100)
    private Long id;

    @Column(name = "CODE", length = 255, nullable = false, unique = true)
    @AdminVisible(order = 200)
    protected String code = UUID.randomUUID().toString();
}