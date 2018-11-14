package com.tinecommerce.admin.panel.controller;

import com.tinecommerce.admin.form.DynamicForm;
import com.tinecommerce.admin.form.DynamicFormField;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sun.reflect.Reflection;

import javax.transaction.Transactional;
import java.awt.geom.Area;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DynamicEntityController {
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
        List<Field> headers = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class) && field.getAnnotation(AdminVisible.class).tableVisible())
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());
        List<? extends AbstractEntity> entities = dynamicEntityDao.findAllPolimorficEntities(className);
        List<AbstractTableLine> abstractTableLines = new ArrayList<>();
        for (AbstractEntity abstractEntity : entities) {
            AbstractTableLine abstractTableLine = new AbstractTableLine();
            for (Field field : headers) {
                for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                    try {
                        Field classField = cls.getDeclaredField(field.getName());
                        classField.setAccessible(true);
                        abstractTableLine.getValues().add(classField.get(abstractEntity));
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
            abstractTableLines.add(abstractTableLine);
        }
        model.addAttribute("headers", headers.stream().map(Field::getName).collect(Collectors.toList()));
        model.addAttribute("abstractTableLines", abstractTableLines);
        model.addAttribute("entityName", entityCode);
        return "tables";
    }

    @GetMapping("/entities/{entityCode}/{id}/edit")
    @Transactional
    public String getDynamicEntityForm(final Model model, @PathVariable(name = "entityCode") String entityCode,
                                       @PathVariable(name = "id") Long id) throws ClassNotFoundException {
        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        model.addAttribute("entityName", entityCode);
        model.addAttribute("id", id);
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        List<Field> fields = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());
        AbstractEntity entity = dynamicEntityDao.findAllPolimorficEntityWithId(className, id);
            DynamicForm dynamicForm = new DynamicForm();
            dynamicForm.setEntityClass(entityCode);
            for (Field field : fields) {
                for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                    try {
                        Field classField = cls.getDeclaredField(field.getName());
                        classField.setAccessible(true);
                        String fieldType = "text";
                        if(classField.getType().getSimpleName().equals("Boolean"))
                        {
                            fieldType = "checkbox";
                        }
                        dynamicForm.getDynamicFormFields().add(new DynamicFormField(fieldType, field.getName(), classField.get(entity)));

                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
        model.addAttribute("dynamicForm", dynamicForm);
        return "dynamicEntityForm";
    }

    @GetMapping("/entities/{entityCode}/add")
    @Transactional
    public String getDynamicEntityAddForm(final Model model, @PathVariable(name = "entityCode") String entityCode) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        model.addAttribute("entityName", entityCode);
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        List<Field> fields = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());
        DynamicForm dynamicForm = new DynamicForm();
        dynamicForm.setEntityClass(entityCode);
        for (Field field : fields) {
            for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                try {
                    Field classField = cls.getDeclaredField(field.getName());
                    classField.setAccessible(true);
                    String fieldType = "text";
                    if(classField.getType().getSimpleName().equals("Boolean")) {
                        fieldType = "checkbox";
                    }
                    if(!field.getName().equals(AbstractEntity.FIELD_ID)) {
                        dynamicForm.getDynamicFormFields().add(new DynamicFormField(fieldType, field.getName(), null));
                    }

                } catch (NoSuchFieldException nsfe) {
                }
            }
        }
        model.addAttribute("dynamicForm", dynamicForm);
        return "dynamicEntityForm";
    }

    @RequestMapping(value = "/entities/{entityCode}/{id}/edit", method = RequestMethod.POST)
    @Transactional
    public String editEntity(final Model model, @PathVariable(name = "entityCode") String entityCode,
                             @PathVariable(name = "id") Long id, @RequestParam Map<String, String> params) throws ClassNotFoundException {
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        List<Field> fields = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());
        AbstractEntity entity = dynamicEntityDao.findAllPolimorficEntityWithId(className, id);
        for (Field field : fields) {
            for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                try {
                    Field classField = cls.getDeclaredField(field.getName());
                    classField.setAccessible(true);
                    if(classField.getType().getSimpleName().equals("Boolean"))
                    {
                        classField.setBoolean(entity, Boolean.parseBoolean(params.get(field.getName())));
                    }
                    if(classField.getType().getSimpleName().equals("String"))
                    {
                        classField.set(entity, params.get(field.getName()));
                    }
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        }
        dynamicEntityDao.save(entity);
        return "redirect:/entities/" + entityCode + "/" + id + "/edit";
    }

    @RequestMapping(value = "/entities/{entityCode}/add", method = RequestMethod.POST)
    @Transactional
    public String addEntity(final Model model, @PathVariable(name = "entityCode") String entityCode, @RequestParam Map<String, String> params) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        List<Field> fields = ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());

        Class<?> clazz = Class.forName(className);
        AbstractEntity entity = (AbstractEntity) clazz.newInstance();
        for (Field field : fields) {
            for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                try {
                    Field classField = cls.getDeclaredField(field.getName());
                    classField.setAccessible(true);
                    if(classField.getType().getSimpleName().equals("Boolean"))
                    {
                        classField.setBoolean(entity, Boolean.parseBoolean(params.get(field.getName())));
                    }
                    if(classField.getType().getSimpleName().equals("String"))
                    {
                        classField.set(entity, params.get(field.getName()));
                    }
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        }
        dynamicEntityDao.save(entity);
        return "redirect:/entities/" + entityCode;
    }

    @RequestMapping(value = "/entities/{entityCode}/{id}/delete", method = RequestMethod.POST)
    @Transactional
    public String deleteEntity(final Model model, @PathVariable(name = "entityCode") String entityCode,
                             @PathVariable(name = "id") Long id) throws ClassNotFoundException {
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        AbstractEntity entity = dynamicEntityDao.findAllPolimorficEntityWithId(className, id);
        dynamicEntityDao.delete(entity);
        return "redirect:/entities/" + entityCode;
    }
}
