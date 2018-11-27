package com.tinecommerce.admin.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RelationMetadata {
    private String foreignKeyName;
    private String relationClass;
    private String type;
    private DynamicEntityTable entityTable;
}
