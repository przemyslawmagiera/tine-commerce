package com.tinecommerce.admin.panel.controller;

import com.tinecommerce.admin.form.DynamicEntityTable;
import com.tinecommerce.admin.form.DynamicForm;
import com.tinecommerce.admin.form.DynamicFormField;
import com.tinecommerce.admin.form.RelationMetadata;
import com.tinecommerce.admin.panel.AbstractTableLine;
import com.tinecommerce.admin.panel.model.AdminMenuItem;
import com.tinecommerce.admin.panel.repository.AdminMenuGroupRepository;
import com.tinecommerce.admin.panel.repository.AdminMenuItemRepository;
import com.tinecommerce.admin.panel.repository.DynamicEntityDao;
import com.tinecommerce.admin.security.model.AdminPermission;
import com.tinecommerce.admin.security.model.AdminUser;
import com.tinecommerce.admin.security.repository.AdminUserRepository;
import com.tinecommerce.core.AbstractEntity;
import com.tinecommerce.core.AdminVisible;
import com.tinecommerce.core.catalog.model.Category;
import com.tinecommerce.core.extension.ExtensionUtil;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
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

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Transactional
    public boolean hasPermissionForOperation(String checkedClassName){
        AdminUser adminUser = adminUserRepository.findByUsername(getLoggedInUsername()).get();
        return getAllChildPermissions(adminUser.getAdminPermissions())
                .stream()
                .map(AdminPermission::getClassName)
                .anyMatch(checkedClassName::equals);
    }

    private Set<AdminPermission> getAllChildPermissions(Set<AdminPermission> adminPermissions) {
        final Set<AdminPermission> result = new HashSet<>();
        adminPermissions.forEach(adminPermission -> getAllChildPermissions(adminPermission, result));
        return result;
    }

    private void getAllChildPermissions(final AdminPermission adminPermission, final Set<AdminPermission> result) {
        adminPermission.getChildPermissions().forEach(child -> this.getAllChildPermissions(child, result));
        result.add(adminPermission);
    }

    @GetMapping
    public String getLoggedInUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElse(null);
    }

    @GetMapping("/entities/{entityCode}")
    @Transactional
    public String getDynamicEntityList(final Model model, @PathVariable(name = "entityCode") String entityCode) throws ClassNotFoundException {
        String className = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        if(hasPermissionForOperation(className)) {
            model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
            List<? extends AbstractEntity> entities = dynamicEntityDao.findAllPolimorficEntities(className);
            DynamicEntityTable entityTable = buildDynamicTable(className, entities);
            model.addAttribute("headers", entityTable.getHeaders());
            model.addAttribute("abstractTableLines", entityTable.getTableLines());
            model.addAttribute("entityName", entityCode);
        } else {
            model.addAttribute("entityName", "you do not have permission");
        }
        return "tables";
    }

    public DynamicEntityTable buildTableFromClassName(String className) throws ClassNotFoundException {
        List<? extends AbstractEntity> entities = dynamicEntityDao.findAllPolimorficEntities(className);
        DynamicEntityTable entityTable = buildDynamicTable(className, entities);
        return entityTable;
    }

    private DynamicEntityTable buildDynamicTable(String className, List<? extends AbstractEntity> entities) throws ClassNotFoundException {
        List<Field> headers = findDynamicHeaders(className);
        List<AbstractTableLine> abstractTableLines = buildAbstractTableLines(entities, className, headers);
        return new DynamicEntityTable(className, "",  headers.stream().map(Field::getName).collect(Collectors.toList()), abstractTableLines);
    }

    private List<Field> findDynamicHeaders(String className) throws ClassNotFoundException {
        return ExtensionUtil.getPolymorphicFielsdOf(className)
                .stream()
                .filter(field -> field.isAnnotationPresent(AdminVisible.class) && field.getAnnotation(AdminVisible.class).tableVisible())
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(AdminVisible.class).order()))
                .collect(Collectors.toList());
    }

    private List<AbstractTableLine> buildAbstractTableLines(List<? extends AbstractEntity> entities, String className, List<Field> headers) throws ClassNotFoundException {
        List<AbstractTableLine> abstractTableLines = new ArrayList<>();
        for (AbstractEntity abstractEntity : entities) {
            AbstractTableLine abstractTableLine = new AbstractTableLine();
            for (Field field : headers) {
                for (Class<?> cls : ExtensionUtil.getSubclassesOf(className)) {
                    try {
                        Field classField = cls.getDeclaredField(field.getName());
                        classField.setAccessible(true);
                        if(classField.get(abstractEntity) == null){
                            abstractTableLine.getValues().add("");
                        } else {
                            abstractTableLine.getValues().add(classField.get(abstractEntity));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
            abstractTableLines.add(abstractTableLine);
        }
        return abstractTableLines;
    }

    @GetMapping("/entities/{entityCode}/{id}/edit")
    @Transactional
    public String getDynamicEntityForm(final Model model, @PathVariable(name = "entityCode") String entityCode,
                                       @PathVariable(name = "id") Long id) throws ClassNotFoundException {
        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        model.addAttribute("entityName", entityCode);
        model.addAttribute("id", id);
        List<RelationMetadata> relationalEntities = new ArrayList<>();
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
                    if (classField.getType().getSimpleName().equals("Boolean")) {
                        fieldType = "checkbox";
                    }
                    if (Collection.class.isAssignableFrom(classField.getType()) && classField.getAnnotation(OneToMany.class) != null) {
                        String foreignKeyName = field.getAnnotation(OneToMany.class).mappedBy();
                        String relationalClassName = field.getAnnotation(AdminVisible.class).className();
                        List<AbstractEntity> lazyCollection = dynamicEntityDao.findAllPolimorficEntitiesWithForeignKey(relationalClassName, foreignKeyName, entity.getId());
                        DynamicEntityTable entityTable = buildDynamicTable(relationalClassName, lazyCollection);
                        entityTable.setName(field.getName());
                        adminMenuItemRepository.findByClassName(relationalClassName).ifPresent(name ->
                                entityTable.setCode(name.getCode()));
                        relationalEntities.add(new RelationMetadata(foreignKeyName, relationalClassName, "o2m", entityTable));
                    } else if (Collection.class.isAssignableFrom(classField.getType()) && classField.getAnnotation(ManyToMany.class) != null) {
                        String foreignKeyName = field.getAnnotation(ManyToMany.class).mappedBy();
                        if(foreignKeyName.equals("")) {
                            foreignKeyName = field.getAnnotation(AdminVisible.class).mappedBy();
                        }
                        String relationalClassName = field.getAnnotation(AdminVisible.class).className();
                        List<AbstractEntity> lazyCollection = dynamicEntityDao.findAllPolimorficEntitiesWithManyToManyRelation(relationalClassName, foreignKeyName, entity.getId());
                        DynamicEntityTable entityTable = buildDynamicTable(relationalClassName, lazyCollection);
                        entityTable.setName(field.getName());
                        adminMenuItemRepository.findByClassName(relationalClassName).ifPresent(name ->
                                entityTable.setCode(name.getCode()));
                        relationalEntities.add(new RelationMetadata(foreignKeyName,relationalClassName, "m2m", entityTable));
                    } else {
                        dynamicForm.getDynamicFormFields().add(new DynamicFormField(fieldType, field.getName(), classField.get(entity)));
                    }

                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        }
        model.addAttribute("relationalEntities", relationalEntities);
        model.addAttribute("dynamicForm", dynamicForm);
        return "dynamicEntityForm";
    }


    @PersistenceContext
    EntityManager entityManager;

    @GetMapping("/entities/{entityCode}/{id}/addRelation/{toManyClass}/{secondId}/{foreignKeyName}/{relationType}")
    @Transactional
    public String addRelation(final Model model, @PathVariable(name = "entityCode") String entityCode,
                                       @PathVariable(name = "id") Long id, @PathVariable(name = "secondId") Long secondId,
                              @PathVariable(name = "toManyClass") String toManyClass, @PathVariable(name = "relationType") String relationType,
                              @PathVariable(name = "foreignKeyName") String foreignKeyName) throws ClassNotFoundException, IllegalAccessException {
        String oneToClass = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        AbstractEntity entity = dynamicEntityDao.findAllPolimorficEntityWithId(toManyClass, secondId);
        AbstractEntity targetEntity = dynamicEntityDao.findAllPolimorficEntityWithId(oneToClass, id);
        if(relationType.equals("o2m"))
        {
            Field relationField = ExtensionUtil.findFieldInPolimorficClass(toManyClass, foreignKeyName);
            relationField.setAccessible(true);
            relationField.set(entity, targetEntity);
            entityManager.persist(entity);
        } else
        {
            Field relationField = ExtensionUtil.findFieldInPolimorficClass(toManyClass, foreignKeyName);
            Field relationField2 = ExtensionUtil.findFieldInPolimorficClass(oneToClass, relationField.getAnnotation(AdminVisible.class).mappedBy());
            relationField.setAccessible(true);
            relationField2.setAccessible(true);
            PersistentSet list = (PersistentSet) relationField.get(entity);
            PersistentSet list2 = (PersistentSet) relationField2.get(targetEntity);
            list.add(targetEntity);
            list2.add(entity);
            relationField.set(entity, list);
            relationField2.set(targetEntity, list2);
        }
        return "redirect:/entities/" + entityCode + "/" + id + "/edit";
    }

    @GetMapping("/entities/{entityCode}/{id}/removeRelation/{toManyClass}/{secondId}/{foreignKeyName}/{relationType}")
    @Transactional
    public String removeRelation(final Model model, @PathVariable(name = "entityCode") String entityCode,
                              @PathVariable(name = "id") Long id, @PathVariable(name = "secondId") Long secondId,
                              @PathVariable(name = "toManyClass") String toManyClass, @PathVariable(name = "relationType") String relationType,
                              @PathVariable(name = "foreignKeyName") String foreignKeyName) throws ClassNotFoundException, IllegalAccessException {

        String oneToClass = adminMenuItemRepository.findByCode(entityCode)
                .map(AdminMenuItem::getClassName)
                .orElse("");
        AbstractEntity entity = dynamicEntityDao.findAllPolimorficEntityWithId(toManyClass, secondId);
        AbstractEntity parent = dynamicEntityDao.findAllPolimorficEntityWithId(oneToClass, id);
        if(relationType.equals("o2m"))
        {
            Field relationField = ExtensionUtil.findFieldInPolimorficClass(toManyClass, foreignKeyName);
            relationField.setAccessible(true);
            relationField.set(entity, null);
            entityManager.persist(entity);
        }
        else
        {
            Field relationField = ExtensionUtil.findFieldInPolimorficClass(toManyClass, foreignKeyName);
            Field relationField2 = ExtensionUtil.findFieldInPolimorficClass(oneToClass, relationField.getAnnotation(AdminVisible.class).mappedBy());
            relationField.setAccessible(true);
            relationField2.setAccessible(true);
            PersistentSet list = (PersistentSet) relationField.get(entity);
            PersistentSet list2 = (PersistentSet) relationField2.get(parent);
            list.remove(parent);
            list2.remove(entity);
            relationField.set(entity, list);
            relationField2.set(parent, list2);
        }
        return "redirect:/entities/" + entityCode + "/" + id + "/edit";
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
                .filter(field -> field.isAnnotationPresent(AdminVisible.class) && field.getAnnotation(AdminVisible.class).tableVisible())
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
                    if (classField.getType().getSimpleName().equals("Boolean")) {
                        fieldType = "checkbox";
                    }
                    if (!field.getName().equals(AbstractEntity.FIELD_ID)) {
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
                    if (classField.getType().getSimpleName().equals("Boolean")) {
                        classField.set(entity, "on".equals(params.get(field.getName())));
                    }
                    if (classField.getType().getSimpleName().equals("String")) {
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
                    if (classField.getType().getSimpleName().equals("Boolean")) {
                        classField.set(entity, params.get(field.getName()).equals("on"));
                    }
                    if (classField.getType().getSimpleName().equals("String")) {
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
