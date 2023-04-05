ALTER TABLE products
DROP COLUMN IF EXISTS article_number,
ADD COLUMN brand varchar(255);