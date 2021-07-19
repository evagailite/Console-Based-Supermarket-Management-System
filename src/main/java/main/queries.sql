
--1st table for creating a user
create TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    budget DECIMAL,
    user_type VARCHAR(50) NOT NULL,
    CONSTRAINT pk_username PRIMARY KEY (username)
);

--2nd table for products in the market
create TABLE IF NOT EXISTS products (
    product_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    quantity DECIMAL NOT NULL,
    price DECIMAL NOT NULL,
    unit VARCHAR(100) NOT NULL,
    product_category VARCHAR(100) NOT NULL
);

create TABLE IF NOT EXISTS sales (
    sales_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    sales_order INTEGER NOT NULL,
    product VARCHAR(100) NOT NULL,
    quantity DECIMAL NOT NULL,
    price DECIMAL NOT NULL,
    username VARCHAR(50) NOT NULL,
    purchase_date DATE NOT NULL
);

select * from SALES;

alter table TEST alter COLUMN PRICE SET DATA TYPE DECIMAL(20,2);
alter table PRODUCTS alter COLUMN QUANTITY SET DATA TYPE INTEGER;

select
ORDER_ID, USERNAME, PURCHASE_DATE,
sum (QUANTITY * PRICE ) as TOTAL
from SALES
where USERNAME = 'test777'
group by ORDER_ID;

--total amount of sales
select
sum (QUANTITY * PRICE ) as TOTAL
from SALES;

-- total amount of orders
select count(distinct ORDER_ID ) from SALES ;


select
ORDER_ID, USERNAME, PURCHASE_DATE,
sum (QUANTITY * PRICE ) as TOTAL
from SALES
group by ORDER_ID;

select * from SALES ;

select PRODUCT ,
sum(QUANTITY ) as TOTAL_QTY
 from SALES
 group by PRODUCT
order by
	TOTAL_QTY desc
LIMIT 3;

--************************
select count(QUANTITY )
from PRODUCTS
where QUANTITY <20;

select count(QUANTITY )
from PRODUCTS
where QUANTITY =0;

ALTER TABLE  TABLE_PRODUCTS DROP  PRODUCT_ID;
ALTER TABLE PRODUCTS ADD PRODUCT_ID  int UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;