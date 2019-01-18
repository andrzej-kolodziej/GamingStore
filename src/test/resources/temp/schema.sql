CREATE CACHED TABLE PUBLIC.PUBLISHER(
ID INTEGER NOT NULL,
DATE_CREATED TIMESTAMP,
LAST_UPDATED TIMESTAMP,
VERSION INTEGER,
DESCRIPTION VARCHAR(255),
IMAGE_URL VARCHAR(255),
NAME VARCHAR(255) NOT NULL);

CREATE CACHED TABLE PUBLIC.DEVELOPER(
    ID INTEGER NOT NULL,
    DATE_CREATED TIMESTAMP,
    LAST_UPDATED TIMESTAMP,
    VERSION INTEGER,
    DESCRIPTION VARCHAR(255),
    IMAGE_URL VARCHAR(255),
    NAME VARCHAR(255)
);

CREATE CACHED TABLE PUBLIC.ORDER_HISTORY(
    ID INTEGER NOT NULL,
    DATE_CREATED TIMESTAMP,
    LAST_UPDATED TIMESTAMP,
    VERSION INTEGER,
    ORDER_TYPE VARCHAR(255),
    TOTAL_PRICE DECIMAL(19, 2),
    USER_ID INTEGER
);

CREATE CACHED TABLE PUBLIC.BUNDLE_PRODUCT(
    BUNDLE_ID INTEGER NOT NULL,
    PRODUCT_ID INTEGER NOT NULL
);

CREATE CACHED TABLE PUBLIC.CART_CART_DETAILS(
    CART_ID INTEGER NOT NULL,
    CART_DETAILS_ID INTEGER NOT NULL
);

	CREATE CACHED TABLE PUBLIC.CART_DETAIL(
    ID INTEGER NOT NULL,
    DATE_CREATED TIMESTAMP,
    LAST_UPDATED TIMESTAMP,
    VERSION INTEGER,
    QUANTITY INTEGER,
    PRODUCT_ID INTEGER
);

CREATE CACHED TABLE PUBLIC.USER(
    ID INTEGER NOT NULL,
    DATE_CREATED TIMESTAMP,
    LAST_UPDATED TIMESTAMP,
    VERSION INTEGER,
    ADDRESS_LINE1 VARCHAR(255),
    ADDRESS_LINE2 VARCHAR(255),
    CITY VARCHAR(255),
    STATE VARCHAR(255),
    ZIP_CODE VARCHAR(255),
    EMAIL VARCHAR(255),
    PASSWORD VARCHAR(255),
    USER_NAME VARCHAR(255),
    CART_ID INTEGER
);

CREATE CACHED TABLE PUBLIC.ORDER_HISTORY_CART_DETAILS(
ORDER_HISTORY_ID INTEGER NOT NULL,
CART_DETAILS_ID INTEGER NOT NULL
);

CREATE CACHED TABLE PUBLIC.BUNDLE(
ID INTEGER NOT NULL,
DATE_CREATED TIMESTAMP,
LAST_UPDATED TIMESTAMP,
VERSION INTEGER,
DESCRIPTION VARCHAR(255),
IMAGE_URL VARCHAR(255),
NAME VARCHAR(255),
PRICE DECIMAL(19, 2)
);

CREATE CACHED TABLE PUBLIC.ROLE(
    ID INTEGER NOT NULL,
    DATE_CREATED TIMESTAMP,
    LAST_UPDATED TIMESTAMP,
    VERSION INTEGER,
    ROLE VARCHAR(255)
);

CREATE CACHED TABLE PUBLIC.USER_ORDER_HISTORIES(
    USER_ID INTEGER NOT NULL,
    ORDER_HISTORIES_ID INTEGER NOT NULL
);

CREATE CACHED TABLE PUBLIC.PRODUCT(
ID INTEGER NOT NULL,
DATE_CREATED TIMESTAMP,
LAST_UPDATED TIMESTAMP,
VERSION INTEGER,
DESCRIPTION VARCHAR(255),
IMAGE_URL VARCHAR(255),
NAME VARCHAR(255),
PRICE DECIMAL(19, 2),
YOUTUBE_URL VARCHAR(255) NOT NULL,
DEVELOPER_ID INTEGER,
PUBLISHER_ID INTEGER
);

CREATE CACHED TABLE PUBLIC.ROLE_USER(
    ROLE_ID INTEGER NOT NULL,
    USER_ID INTEGER NOT NULL
);

CREATE CACHED TABLE PUBLIC.CART(
ID INTEGER NOT NULL,
DATE_CREATED TIMESTAMP,
LAST_UPDATED TIMESTAMP,
VERSION INTEGER,
USER_ID INTEGER
);


ALTER TABLE PUBLIC.ROLE_USER ADD CONSTRAINT PUBLIC.FKIQPMJD2QB4RDKEJ916YMONIC6 FOREIGN KEY(ROLE_ID) INDEX PUBLIC.FKIQPMJD2QB4RDKEJ916YMONIC6_INDEX_B REFERENCES PUBLIC.ROLE(ID) NOCHECK;
ALTER TABLE PUBLIC.CART_CART_DETAILS ADD CONSTRAINT PUBLIC.UK_NNOYIPX28SGNGM36P8U21WD9E UNIQUE(CART_DETAILS_ID);
ALTER TABLE PUBLIC.CART_DETAIL ADD CONSTRAINT PUBLIC.CONSTRAINT_A PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_A;
ALTER TABLE PUBLIC.CART_CART_DETAILS ADD CONSTRAINT PUBLIC.FKMK88NOMOHN3SIFXXW13XCC3H2 FOREIGN KEY(CART_DETAILS_ID) INDEX PUBLIC.UK_NNOYIPX28SGNGM36P8U21WD9E_INDEX_9 REFERENCES PUBLIC.CART_DETAIL(ID) NOCHECK;
ALTER TABLE PUBLIC.ROLE ADD CONSTRAINT PUBLIC.CONSTRAINT_2 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_2;
ALTER TABLE PUBLIC.ORDER_HISTORY_CART_DETAILS ADD CONSTRAINT PUBLIC.UK_LKMFOXUD14PYMIIMNYQX9MBWP UNIQUE(CART_DETAILS_ID);
ALTER TABLE PUBLIC.USER_ORDER_HISTORIES ADD CONSTRAINT PUBLIC.UK_OLIECN8R6XP1VMVL359UI9WGR UNIQUE(ORDER_HISTORIES_ID);
ALTER TABLE PUBLIC.BUNDLE ADD CONSTRAINT PUBLIC.CONSTRAINT_7 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_7;
ALTER TABLE PUBLIC.USER ADD CONSTRAINT PUBLIC.CONSTRAINT_27 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_27;
ALTER TABLE PUBLIC.USER ADD CONSTRAINT PUBLIC.UK_OB8KQYQQGMEFL0ACO34AKDTPE UNIQUE(EMAIL) INDEX PUBLIC.UK_OB8KQYQQGMEFL0ACO34AKDTPE_INDEX_2;
ALTER TABLE PUBLIC.USER_ORDER_HISTORIES ADD CONSTRAINT PUBLIC.FKMQX7DTOXMP9NRNMW0SWLS5KAX FOREIGN KEY(USER_ID) INDEX PUBLIC.FKMQX7DTOXMP9NRNMW0SWLS5KAX_INDEX_4 REFERENCES PUBLIC.USER(ID) NOCHECK;
ALTER TABLE PUBLIC.BUNDLE_PRODUCT ADD CONSTRAINT PUBLIC.CONSTRAINT_72 PRIMARY KEY(BUNDLE_ID, PRODUCT_ID) INDEX PUBLIC.PRIMARY_KEY_72;
ALTER TABLE PUBLIC.CART ADD CONSTRAINT PUBLIC.CONSTRAINT_1 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_1;
ALTER TABLE PUBLIC.CART_CART_DETAILS ADD CONSTRAINT PUBLIC.FKBY8B0EPEO45CWI161X9HJLHFR FOREIGN KEY(CART_ID) INDEX PUBLIC.FKBY8B0EPEO45CWI161X9HJLHFR_INDEX_9 REFERENCES PUBLIC.CART(ID) NOCHECK;
ALTER TABLE PUBLIC.BUNDLE_PRODUCT ADD CONSTRAINT PUBLIC.FKMK7V8HX1I7E92Q3U4M27WJ8PH FOREIGN KEY(BUNDLE_ID) INDEX PUBLIC.FKMK7V8HX1I7E92Q3U4M27WJ8PH_INDEX_7 REFERENCES PUBLIC.BUNDLE(ID) NOCHECK;
ALTER TABLE PUBLIC.ORDER_HISTORY_CART_DETAILS ADD CONSTRAINT PUBLIC.FK5HI442NQJ7LY7TALR3T8I9EP3 FOREIGN KEY(CART_DETAILS_ID) INDEX PUBLIC.UK_LKMFOXUD14PYMIIMNYQX9MBWP_INDEX_D REFERENCES PUBLIC.CART_DETAIL(ID) NOCHECK;
ALTER TABLE PUBLIC.ROLE_USER ADD CONSTRAINT PUBLIC.FK4320P8BGVUMLXJKOHTBJ214QI FOREIGN KEY(USER_ID) INDEX PUBLIC.FK4320P8BGVUMLXJKOHTBJ214QI_INDEX_B REFERENCES PUBLIC.USER(ID) NOCHECK;
ALTER TABLE PUBLIC.CART_DETAIL ADD CONSTRAINT PUBLIC.FK37HAI783JHFCQO6H0PKIQMC9S FOREIGN KEY(PRODUCT_ID) INDEX PUBLIC.FK37HAI783JHFCQO6H0PKIQMC9S_INDEX_A REFERENCES PUBLIC.PRODUCT(ID) NOCHECK;
ALTER TABLE PUBLIC.DEVELOPER ADD CONSTRAINT PUBLIC.CONSTRAINT_A1 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_A1;
ALTER TABLE PUBLIC.USER ADD CONSTRAINT PUBLIC.UK_LQJRCOBRH9JC8WPCAR64Q1BFH UNIQUE(USER_NAME) INDEX PUBLIC.UK_LQJRCOBRH9JC8WPCAR64Q1BFH_INDEX_2	;
ALTER TABLE PUBLIC.ORDER_HISTORY ADD CONSTRAINT PUBLIC.FKP03GUO9HM9UF9K0N4A1SAM969 FOREIGN KEY(USER_ID) INDEX PUBLIC.FKP03GUO9HM9UF9K0N4A1SAM969_INDEX_E REFERENCES PUBLIC.USER(ID) NOCHECK;
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.CONSTRAINT_18 PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_18;
ALTER TABLE PUBLIC.ORDER_HISTORY_CART_DETAILS ADD CONSTRAINT PUBLIC.FKNWTKXJP2SS7PWEB3PIP9R1K8X FOREIGN KEY(ORDER_HISTORY_ID) INDEX PUBLIC.FKNWTKXJP2SS7PWEB3PIP9R1K8X_INDEX_D REFERENCES PUBLIC.ORDER_HISTORY(ID) NOCHECK	;
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.FK78LIHPJT4DPGIB97DG9DL3XE6 FOREIGN KEY(DEVELOPER_ID) INDEX PUBLIC.FK78LIHPJT4DPGIB97DG9DL3XE6_INDEX_1 REFERENCES PUBLIC.DEVELOPER(ID) NOCHECK;
ALTER TABLE PUBLIC.USER_ORDER_HISTORIES ADD CONSTRAINT PUBLIC.FKPQYDLH944CVPHQYN7WCL6DHRD FOREIGN KEY(ORDER_HISTORIES_ID) INDEX PUBLIC.UK_OLIECN8R6XP1VMVL359UI9WGR_INDEX_4 REFERENCES PUBLIC.ORDER_HISTORY(ID) NOCHECK;
ALTER TABLE PUBLIC.CART ADD CONSTRAINT PUBLIC.FKL70ASP4L4W0JMBM1TQYOFHO4O FOREIGN KEY(USER_ID) INDEX PUBLIC.FKL70ASP4L4W0JMBM1TQYOFHO4O_INDEX_1 REFERENCES PUBLIC.USER(ID) NOCHECK;
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.FKJFG1NL4BF425EVNMY1CY8DD9E FOREIGN KEY(PUBLISHER_ID) INDEX PUBLIC.FKJFG1NL4BF425EVNMY1CY8DD9E_INDEX_1 REFERENCES PUBLIC.PUBLISHER(ID) NOCHECK;
ALTER TABLE PUBLIC.USER ADD CONSTRAINT PUBLIC.FKTQA69BIB34K2C0JHE7AFQSAO6 FOREIGN KEY(CART_ID) INDEX PUBLIC.FKTQA69BIB34K2C0JHE7AFQSAO6_INDEX_2 REFERENCES PUBLIC.CART(ID) NOCHECK;
ALTER TABLE PUBLIC.ORDER_HISTORY ADD CONSTRAINT PUBLIC.CONSTRAINT_E PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_E;
ALTER TABLE PUBLIC.PUBLISHER ADD CONSTRAINT PUBLIC.CONSTRAINT_F PRIMARY KEY(ID) INDEX PUBLIC.PRIMARY_KEY_F;
ALTER TABLE PUBLIC.BUNDLE_PRODUCT ADD CONSTRAINT PUBLIC.FKJR0AI6MIP6WH7XRM3MW5YJ88M FOREIGN KEY(PRODUCT_ID) INDEX PUBLIC.FKJR0AI6MIP6WH7XRM3MW5YJ88M_INDEX_7 REFERENCES PUBLIC.PRODUCT(ID) NOCHECK;