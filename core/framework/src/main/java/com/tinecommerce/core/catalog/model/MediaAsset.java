package com.tinecommerce.core.catalog.model;

import com.tinecommerce.core.AbstractNameableEntity;
import com.tinecommerce.core.AdminVisible;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_asset")
@Getter
@Setter
public class MediaAsset extends AbstractNameableEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "url")
    @AdminVisible
    private String url;

    public MediaAsset(String url, String name) {
        this.setName(name);
        this.url = url;
    }

}
