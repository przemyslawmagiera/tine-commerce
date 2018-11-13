package com.tinecommerce.admin.form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DynamicForm {
    private List<DynamicFormField> dynamicFormFields = new ArrayList<>();
    private String entityClass;
}
