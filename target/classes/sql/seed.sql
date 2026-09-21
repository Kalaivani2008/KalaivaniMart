-- Demo Users

INSERT INTO users
(id, name, email, password_hash, role)
VALUES
(1, 'Admin', 'admin@stationerymart.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'ADMIN');

INSERT INTO users
(id, name, email, password_hash, role)
VALUES
(2, 'Stationery Seller', 'seller@stationerymart.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'SELLER');

INSERT INTO users
(id, name, email, password_hash, role)
VALUES
(3, 'Demo Buyer', 'buyer@stationerymart.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'BUYER');


-- Demo Products

INSERT INTO products
(seller_id, name, description, price, stock_qty, category)
VALUES
(2, 'Ball Pen',
 'Smooth blue ink ball pen for everyday writing',
 10.00, 100, 'Writing');

INSERT INTO products
(seller_id, name, description, price, stock_qty, category)
VALUES
(2, 'Notebook',
 'A5 ruled notebook with 200 pages',
 80.00, 50, 'Books');

INSERT INTO products
(seller_id, name, description, price, stock_qty, category)
VALUES
(2, 'Pencil Box',
 'Durable pencil box for school and college use',
 120.00, 30, 'School Supplies');

INSERT INTO products
(seller_id, name, description, price, stock_qty, category)
VALUES
(2, 'Highlighter Set',
 'Set of colourful highlighters for notes and study',
 90.00, 40, 'Writing');

INSERT INTO products
(seller_id, name, description, price, stock_qty, category)
VALUES
(2, 'Geometry Box',
 'Complete geometry box with essential mathematical instruments',
 150.00, 25, 'School Supplies');