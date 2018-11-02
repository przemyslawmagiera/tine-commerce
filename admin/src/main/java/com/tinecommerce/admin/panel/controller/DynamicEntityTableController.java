package com.tinecommerce.admin.panel.controller;

import com.tinecommerce.admin.panel.AbstractTableLine;
import com.tinecommerce.admin.panel.model.AdminMenuItem;
import com.tinecommerce.admin.panel.repository.AdminMenuGroupRepository;
import com.tinecommerce.admin.panel.repository.AdminMenuItemRepository;
import com.tinecommerce.admin.panel.repository.DynamicEntityDao;
import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.extension.ExtensionUtil;
import com.tinecommerce.core.solr.model.SearchField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.reflect.Reflection;

import java.awt.geom.Area;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class DynamicEntityTableController {
    @Autowired
    private AdminMenuGroupRepository adminMenuGroupRepository;

    @Autowired
    private AdminMenuItemRepository adminMenuItemRepository;

    @Autowired
    private DynamicEntityDao dynamicEntityDao;

    @GetMapping("/entities/{entityCode}")
    public String getDynamicEntityList(final Model model, @PathVariable(name = "entityCode") String entityCode) throws ClassNotFoundException {
        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        Set<Field> headers = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class) && field.getAnnotation(AdminVisible.class).tableVisible())
                .collect(Collectors.toSet());
        List<? extends AbstractEntity> entities = dynamicEntityDao.findAllPolimorficEntities(className);
        List<AbstractTableLine> abstractTableLines = new ArrayList<>();
        for (AbstractEntity abstractEntity : entities) {
            AbstractTableLine abstractTableLine = new AbstractTableLine();
            for (Field field : headers) {
                for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                    try {
                        Field classField = cls.getDeclaredField(field.getName());
                        classField.setAccessible(true);
                        abstractTableLine.getValues().add((String) classField.get(abstractEntity));
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
            abstractTableLines.add(abstractTableLine);
        }
        model.addAttribute("headers", headers.stream().map(Field::getName).collect(Collectors.toList()));
        model.addAttribute("abstractTableLines", abstractTableLines);
        return "tables";
    }
}
