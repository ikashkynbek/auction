CREATE TABLE auctions (
  id           SERIAL PRIMARY KEY,
  product_name VARCHAR(500) NOT NULL,
  start_date   TIMESTAMP,
  end_date     TIMESTAMP,
  status       VARCHAR(100) NOT NULL,
  created      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE orders (
  id         SERIAL PRIMARY KEY,
  auction_id INT            NOT NULL,
  customer   VARCHAR(500)   NOT NULL,
  merchant   VARCHAR(500)   NOT NULL,
  price      DECIMAL(10, 2) NOT NULL,
  created    TIMESTAMP      NOT NULL DEFAULT NOW(),
  FOREIGN KEY (auction_id) REFERENCES auctions (id)
);

CREATE TABLE quotes (
  id         SERIAL PRIMARY KEY,
  auction_id INT            NOT NULL,
  owner      VARCHAR(500)   NOT NULL,
  qty        INT            NOT NULL,
  leaves_qty INT            NOT NULL,
  price      DECIMAL(10, 2) NOT NULL,
  type       VARCHAR(100)   NOT NULL,
  status     VARCHAR(100)   NOT NULL,
  created    TIMESTAMP      NOT NULL DEFAULT NOW(),
  FOREIGN KEY (auction_id) REFERENCES auctions (id)
);

CREATE TABLE USERS (
  id       SERIAL PRIMARY KEY,
  login    VARCHAR(50) UNIQUE     NOT NULL,
  password VARCHAR(500)           NOT NULL,
  name     VARCHAR(50)            NOT NULL,
  role     VARCHAR(200)           NOT NULL,
  created  TIMESTAMP              NOT NULL DEFAULT NOW()
);

CREATE TABLE PRODUCTS (
  id      SERIAL PRIMARY KEY,
  name    VARCHAR(500)
);

CREATE TABLE PRODUCT_PROPERTIES (
  id              SERIAL PRIMARY KEY,
  property_name   VARCHAR(500),
  property_value  VARCHAR(500),
  product_id      INT             NOT NULL,
  FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
);

CREATE VIEW order_book AS
  SELECT
    leaves_qty bid,
    price,
    NULL       offer
  FROM quotes
  WHERE type = 'BID' AND STATUS IN ('CREATED', 'PARTIALLY_FILLED') AND quotes.auction_id=1

  UNION ALL

  SELECT
    NULL bid,
    price,
    leaves_qty       offer
  FROM quotes
  WHERE type = 'OFFER' AND STATUS IN ('CREATED', 'PARTIALLY_FILLED') AND quotes.auction_id=1

  ORDER BY price DESC;

CREATE VIEW auction_details AS
  SELECT
    a.product_name AS product_name,
    o.orders       AS orders_count,
    o.max          AS order_max,
    o.min          AS order_min,
    q.min          AS best_offer
  FROM auctions a
    LEFT JOIN (SELECT
                 o2.auction_id,
                 max(price) AS max,
                 min(price) AS min,
                 count(*)   AS orders
               FROM orders o2
               GROUP BY o2.auction_id) o ON a.id = o.auction_id
    LEFT JOIN (SELECT
                 q2.auction_id,
                 min(price) AS min
               FROM quotes q2
               WHERE q2.type = 'OFFER' AND q2.status IN ('CREATED', 'PARTIALLY_FILLED')
               GROUP BY q2.auction_id) q ON a.id = q.auction_id;