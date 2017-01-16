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