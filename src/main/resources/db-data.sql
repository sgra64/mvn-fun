
INSERT INTO CUSTOMER (ID, NAME, FIRSTNAME, CONTACT, STATUS) VALUES
    (1, 'Meyer', 'Eric', 'eme22@gmail.com', 'Active'),
    (2, 'Sommer', 'Tina', '030 22458 29425', 'Active'),
    (3, 'Schulze', 'Tim', '+49 171 2358124', 'Active')
;
ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 4;

INSERT INTO VEHICLE (ID, MAKE, MODEL, SEATS, CATEGORY, POWER, STATUS) VALUES
    (1001, 'VW', 'Golf', 4, 'Sedan', 'Gasoline', 'Active'),
    (1002, 'VW', 'Golf', 4, 'Sedan', 'Hybrid', 'Active'),
    (2000, 'BMW', '320d', 4, 'Sedan', 'Diesel', 'Active'),
    (3000, 'Mercedes', 'EQS', 4, 'Sedan', 'Electric', 'Active'),
    (1200, 'VW', 'Multivan Life', 8, 'Van', 'Gasoline', 'Active'),
    (6000, 'Tesla', 'Model 3', 4, 'Sedan', 'Electric', 'Active'),
    (6001, 'Tesla', 'Model S', 4, 'Sedan', 'Electric', 'Serviced')
;
ALTER TABLE VEHICLE ALTER COLUMN ID RESTART WITH 6002;

INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, TIME_BEGIN, TIME_END, PICKUP, DROPOFF, STATUS) VALUES
    (201235, 3, 1002, {ts '2025-11-17 10:00'}, {ts '2025-11-17 18:00:00.000'}, 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (145373, 2, 6001, {ts '2025-11-18 08:00'}, {ts '2025-11-20 08:00'}, 'Berlin Wedding', 'Hamburg', 'Booked'),
    (382565, 2, 3000, {ts '2025-11-16 09:00'}, {ts '2025-11-17 09:00'}, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (351682, 2, 6000, {ts '2025-11-14 10:00'}, {ts '2025-11-17 16:30'}, 'Berlin Wedding', 'Hamburg', 'Cancelled'),
    (682351, 2, 6000, {ts '2025-11-15 10:00'}, {ts '2025-11-16 20:00'}, 'Potsdam', 'Teltow', 'Booked')
;
ALTER TABLE RESERVATION ALTER COLUMN ID RESTART WITH 682352;
