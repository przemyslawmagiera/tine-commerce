package com.tinecommerce.admin.panel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AbstractTableLine {
    private List<String> values = new ArrayList<>();
}
