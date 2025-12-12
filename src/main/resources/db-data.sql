
INSERT INTO CUSTOMER(ID, NAME, FIRSTNAME, CONTACT, STATUS, STATUS_CHANGE) VALUES
    (100, 'Eric',   'Meyer',    'eme22@gmail.com',      'Active',         {ts '2024-06-04 12:35'}),
    (101, 'Sommer', 'Tina',     '+49 030 22458 29425',  'Active',         {ts '2025-10-07 10:28'}),
    (102, 'Schulze','Tim',      '+49 171 2358124',      'Active',         {ts '2024-12-28 18:00'}),
    (103, 'Brinkmann', 'Tobias','+49 030 662465724',    'InRegistration', {ts '2025-11-28 12:18'}),
    (104, 'Tony',   'Allister', '+49 030 24253134',     'Active',         {ts '2023-02-10 18:00'}),
    (105, 'Sandra', 'Ohlstadt', 'ohlst@gmail.com',      'Active',         {ts '2023-08-17 18:00'}),
    (106, 'Erica',  'Gronemann','gronemann@gmx.de',     'InRegistration', {ts '2022-02-26 07:02'}),
    (107, 'Khaleed','Samadi',   '-',                    'Active',         {ts '2020-09-24 18:00'}),
    (108, 'Igor',   'Medwedev', 'gopnik@bht-berlin.de', 'InRegistration', {ts '2025-11-28 23:26'})
;
ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 109;


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
    (201235, 103, 1002, {ts '2025-11-17 10:00'}, {ts '2025-11-17 18:00:00.000'}, 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (145373, 102, 6001, {ts '2025-11-18 08:00'}, {ts '2025-11-20 08:00'}, 'Berlin Wedding', 'Hamburg', 'Booked'),
    (382565, 102, 3000, {ts '2025-11-16 09:00'}, {ts '2025-11-17 09:00'}, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (351682, 102, 6000, {ts '2025-11-14 10:00'}, {ts '2025-11-17 16:30'}, 'Berlin Wedding', 'Hamburg', 'Cancelled'),
    (682351, 102, 6000, {ts '2025-11-15 10:00'}, {ts '2025-11-16 20:00'}, 'Potsdam', 'Teltow', 'Booked')
;
ALTER TABLE RESERVATION ALTER COLUMN ID RESTART WITH 682352;
