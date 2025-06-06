INSERT INTO RESERVATION (CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
    (1000, 8002, STR_TO_DATE('20/07/2025 10:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('20/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (1001, 8002, STR_TO_DATE('04/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('04/07/2025 23:00:00','%d/%m/%Y %H:%i:%s'), 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1000, 8006, 20250718180000, 20250718181000, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1002, 8001, from_unixtime(unix_timestamp()), from_unixtime(unix_timestamp() + 2*60*60), 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (1002, 8003, from_unixtime(1752829200), from_unixtime(1752861600), 'Potsdam', 'Teltow', 'Inquired')
;
