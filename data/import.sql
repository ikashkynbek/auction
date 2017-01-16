INSERT INTO users (login, name, role, created, password) VALUES ('admin', 'Admin', 'ADMIN', now(),
                                                                     'fef9adee173bf0aa1e3e72f7ed9dd1f2a8103a409d3c96de5febaf4d78e59b60963aedae14d54bde5a261d55e328786e463239ed47ba9eaba7d83014d099c7d6');

INSERT INTO PRODUCTS (name) VALUES ('LG G3');
INSERT INTO PRODUCTS (name) VALUES ('iPhone 6');
INSERT INTO PRODUCTS (name) VALUES ('NEXUS 5x');

INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('WEIGHT', '0.5kg', (SELECT id FROM products WHERE name='LG G3'));
INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('OS', 'Android', (SELECT id FROM products WHERE name='LG G3'));

INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('WEIGHT', '0.2kg', (SELECT id FROM products WHERE name='iPhone 6'));
INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('OS', 'iOS', (SELECT id FROM products WHERE name='iPhone 6'));

INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('WEIGHT', '0.3kg', (SELECT id FROM products WHERE name='NEXUS 5x'));
INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('OS', 'Android', (SELECT id FROM products WHERE name='NEXUS 5x'));
INSERT INTO product_properties (property_name, property_value, product_id) VALUES ('SCREEN', '5inch', (SELECT id FROM products WHERE name='NEXUS 5x'));