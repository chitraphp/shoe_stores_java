#The Shoe Stores App list out local shoe stores and the brands of shoes they carry.

**In PSQL:**

* CREATE DATABASE shoe_stores;
* CREATE TABLE stores (id SERIAL PRIMARY KEY, name VARCHAR(50));
* CREATE TABLE brands (id SERIAL PRIMARY KEY, name VARCHAR(50));
* CREATE TABLE stores_brands (id SERIAL PRIMARY KEY, store_id INT, brand_id INT);
* CREATE DATABASE to_do_test WITH TEMPLATE to_do;

***Setup Instructions:***
1.clone the application
2.Go to terminal and enter "gradle run"
3.Enter URL "localhost:4567" on any browser

License: MIT

*** Technologies used ***

Java version 1.8
Spark 2.2
Junit 4.+
Fluentlenium 0.10.3
Gradle
postgresql
Bootstrap
