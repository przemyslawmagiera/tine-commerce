package com.tinecommerce.admin.form;

import com.tinecommerce.admin.panel.AbstractTableLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DynamicEntityTable {
    private String name;
    private List<String> headers;
    private List<AbstractTableLine> tableLines;
}
