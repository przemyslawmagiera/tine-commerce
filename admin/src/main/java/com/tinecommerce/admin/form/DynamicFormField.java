package com.tinecommerce.admin.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DynamicFormField {
    private String type;
    private String name;
    private Object value;
}
