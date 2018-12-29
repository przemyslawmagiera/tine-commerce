insert into admin_menu_group values 
(-1001, 'catalog', 'Catalog', 10),
(-1002, 'system', 'System', 10),
(-1003, 'security', 'Security', 10),
(-1004, 'commerce', 'Commerce', 10),
(-1005, 'classSystem', 'Classification system', 5);

insert into admin_menu_item (id, code, name, order_number, class_name, friendly_name, group_id) values
(-1001, 'product', 'Product',10, 'com.tinecommerce.core.catalog.model.Product', 'Products', -1001),
(-1002, 'category', 'Category',10, 'com.tinecommerce.core.catalog.model.Category', 'Categories', -1001),
(-1003, 'price', 'Price',10, 'com.tinecommerce.core.catalog.model.Price', 'Prices', -1001),
(-1005, 'customer', 'Customer',10, 'com.tinecommerce.core.customer.model.Customer', 'Customers', -1004),
(-1006, 'order', 'Order',10, 'com.tinecommerce.core.cart.Order', 'Orders', -1004),
(-1007, 'categoryFeatureAssignment', 'CategoryFeatureAssignment',10, 'com.tinecommerce.core.catalog.model.CategoryFeatureAssignment', 'Feature Assignments', -1005),
(-1008, 'categoryFeatureValue', 'CategoryFeatureValue',10, 'com.tinecommerce.core.catalog.model.CategoryFeatureValue', 'Feature Values', -1005),
(-1009, 'categoryFeature', 'CategoryFeature',10, 'com.tinecommerce.core.catalog.model.CategoryFeature', 'Category Features', -1005),
(-1010, 'adminGroupItem', 'AdminGroupItem',10, 'com.tinecommerce.admin.panel.model.AdminMenuItem', 'Admin group items', -1002),
(-1011, 'adminGroup', 'AdminGroup',10, 'com.tinecommerce.admin.panel.model.AdminMenuGroup', 'Admin groups', -1002),
(-1012, 'mediaAsset', 'MediaAsset',10, 'com.tinecommerce.core.catalog.model.MediaAsset', 'Media Assets', -1002),
(-1013, 'adminUser', 'AdminUser',10, 'com.tinecommerce.admin.security.model.AdminUser', 'Admin users', -1003),
(-1014, 'adminPermission', 'AdminPermission',10, 'com.tinecommerce.admin.security.model.AdminPermission', 'Admin permissions', -1003),
(-1015, 'productFeature', 'ProductFeature',10, 'com.tinecommerce.core.catalog.model.ProductFeature', 'Product features', -1005);

insert into admin_permission(id,code,description,name,parent_permission_id,class_name) values
  (1,'1','Root admin permission','Root permission [ROOT]',null,''''),
  (2,'catalogPermission','catalog section permission','Catalog Permission',1,''''),
  (3,'systemPermissions','system section permission','System Permissions',1,''''),
  (4,'securityPermissions','security section permissions','Security permissions',1,''''),
  (5,'commercePermissions','permissions for commerce section','Commerce Permissions',1,''''),
  (6,'classSystemPermissions','Classification system section permissions','Classification system permissions',1,''''),
  (7,'product','''','Product',2,'com.tinecommerce.core.catalog.model.Product'),
  (8,'category','''','Category',2,'com.tinecommerce.core.catalog.model.Category'),
  (9,'price','''','Price',2,'com.tinecommerce.core.catalog.model.Price'),
  (10,'customer','''','Customer',5,'com.tinecommerce.core.customer.model.Customer'),
  (11,'order','''','Order',5,'com.tinecommerce.core.cart.Order'),
  (12,'categoryFeatureAssignment','''','Category Feature Assignment',6,'com.tinecommerce.core.catalog.model.CategoryFeatureAssignment'),
  (13,'categoryFeatureValue','''','Category Feature Value',6,'com.tinecommerce.core.catalog.model.CategoryFeatureValue'),
  (14,'categoryFeature','''','Category Feature',6,'com.tinecommerce.core.catalog.model.CategoryFeature'),
  (15,'adminMenuItem','''','Admin Menu Item',3,'com.tinecommerce.admin.panel.model.AdminMenuItem'),
  (16,'adminMenuGroup','''','Admin Menu Group',3,'com.tinecommerce.admin.panel.model.AdminMenuGroup'),
  (17,'mediaAsset','''','Media Asset',3,'com.tinecommerce.core.catalog.model.MediaAsset'),
  (18,'adminUser','''','Admin User',4,'com.tinecommerce.admin.security.model.AdminUser'),
  (19,'adminPermission','''','Admin Permission',4,'com.tinecommerce.admin.security.model.AdminPermission'),
  (20,'productFeature','''','Product Feature',6,'com.tinecommerce.core.catalog.model.ProductFeature');


 