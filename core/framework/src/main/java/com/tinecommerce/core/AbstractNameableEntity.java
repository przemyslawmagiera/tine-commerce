package com.tinecommerce.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@ToString(of = {"name"})
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractNameableEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2968157318009412565L;

    @Size(max = 255)
    @Column(name = "name")
    @AdminVisible(order = 300)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    @AdminVisible(order = 400)
    private String description;
}