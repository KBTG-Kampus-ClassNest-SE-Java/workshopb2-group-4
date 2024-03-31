CREATE TABLE IF NOT EXISTS cart (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	discount DECIMAL(10, 2) NOT NULL,
	total_discount INT NOT NULL,
	promotion_codes VARCHAR(255),
	sub_total DECIMAL(10, 2) NOT NULL ,
	grand_total DECIMAL(10, 2) NOT NULL
);