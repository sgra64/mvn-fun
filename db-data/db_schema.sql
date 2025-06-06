USE FREERIDER_DB;

DROP TABLE CUSTOMER;
DROP TABLE VEHICLE;
DROP TABLE RESERVATION;

CREATE TABLE if not exists CUSTOMER (
    ID INT not null auto_increment,
    NAME VARCHAR(60) default null,
    CONTACT VARCHAR(60) default null,
    STATUS ENUM('Active', 'InRegistration', 'Terminated') default null,
    PRIMARY KEY ( ID )
);

CREATE TABLE if not exists VEHICLE (
    ID INT not null auto_increment,
    MAKE VARCHAR(60) default null,
    MODEL VARCHAR(60) default null,
    SEATS INT DEFAULT '4',
    CATEGORY ENUM('Sedan', 'SUV', 'Convertible', 'Van', 'Bike') default null,
    POWER ENUM('Gasoline', 'Diesel', 'Electric', 'Hybrid', 'Hydrogen') default null,
    STATUS ENUM('Active', 'Serviced', 'Terminated') default null,
    PRIMARY KEY ( ID )
);

CREATE TABLE if not exists RESERVATION (
    ID INT not null auto_increment,
    CUSTOMER_ID INT not null,
    VEHICLE_ID INT not null,
    RBEGIN DATETIME default null,
    REND DATETIME default null,
    PICKUP VARCHAR(48) default null,
    DROPOFF VARCHAR(48) default null,
    STATUS ENUM('Inquired', 'InquiryConfirmed', 'Booked', 'Cancelled') default null,
    PRIMARY KEY ( ID ),
    FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID),
    FOREIGN KEY (VEHICLE_ID) REFERENCES VEHICLE(ID)
);

DROP TABLE CUSTOMER;

DELETE FROM CUSTOMER;
ALTER TABLE CUSTOMER AUTO_INCREMENT = 1000;
-- 
INSERT INTO CUSTOMER (NAME, CONTACT, STATUS) VALUES
    ('Meyer, Eric', 'eme22@gmail.com', 'Active'),
    ('Sommer, Tina', '030 22458 29425', 'Active'),
    ('Schulze, Tim', '+49 171 2358124', 'Active')
;

DELETE FROM VEHICLE;
ALTER TABLE VEHICLE AUTO_INCREMENT = 8000;
-- 
INSERT INTO VEHICLE (MAKE, MODEL, SEATS, CATEGORY, POWER, STATUS) VALUES
    ('VW      ', 'Golf         ', 4, 'Sedan', 'Gasoline', 'Active'),
    ('VW      ', 'Golf         ', 4, 'Sedan', 'Hybrid'  , 'Active'),
    ('VW      ', 'Multivan Life', 8, 'Van'  , 'Gasoline', 'Active'),
    ('BMW     ', '320d         ', 4, 'Sedan', 'Diesel'  , 'Active'),
    ('Mercedes', 'EQS          ', 4, 'Sedan', 'Electric', 'Active'),
    ('Tesla   ', 'Model 3      ', 4, 'Sedan', 'Electric', 'Active'),
    ('Tesla   ', 'Model S      ', 4, 'Sedan', 'Electric', 'Serviced')
;

DELETE FROM RESERVATION;
ALTER TABLE RESERVATION AUTO_INCREMENT = 10000;
-- 
INSERT INTO RESERVATION (CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
    (1000, 8002, STR_TO_DATE('20/07/2025 10:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('20/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (1001, 8002, STR_TO_DATE('04/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('04/07/2025 23:00:00','%d/%m/%Y %H:%i:%s'), 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1000, 8006, 20250718180000, 20250718181000, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1002, 8001, from_unixtime(unix_timestamp()), from_unixtime(unix_timestamp() + 2*60*60), 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1002, 8003, from_unixtime(1752829200), from_unixtime(1752861600), 'Potsdam', 'Teltow', 'Inquired')
;

SELECT * FROM CUSTOMER;
SELECT * FROM VEHICLE;
SELECT * FROM RESERVATION;
