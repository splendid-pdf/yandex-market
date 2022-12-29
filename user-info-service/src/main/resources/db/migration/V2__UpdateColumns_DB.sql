ALTER TABLE users
ALTER COLUMN first_name TYPE varchar(50) using not null,
ALTER COLUMN middle_name TYPE varchar(70),
ALTER COLUMN last_name TYPE varchar(100) using not null,
ALTER COLUMN phone TYPE varchar(20),
ALTER COLUMN postcode type varchar(40),
ALTER COLUMN sex type varchar(15);


