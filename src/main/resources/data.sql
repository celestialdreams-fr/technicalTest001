DROP TABLE IF EXISTS users;
 
CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(40) NOT NULL,
  birth_date DATE NOT NULL,
  country_code VARCHAR(2) NOT NULL,
  gender VARCHAR(1) NULL,
  phone_number VARCHAR(16) NULL
);
 
INSERT INTO users (name, birth_date, country_code, gender, phone_number) VALUES
  ('Arnaud', '1975-01-01', 'FR', 'M', '+337777777'),
  ('Maria', '2016-10-10', 'DE', 'F', '+336666666'),
  ('Victoria', '2010-02-11', 'US', 'F', '+3311111111');
