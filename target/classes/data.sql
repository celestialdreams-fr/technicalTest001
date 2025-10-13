DROP TABLE IF EXISTS users;
 
CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(40) NOT NULL,
  birth_date DATE NOT NULL,
  country_code VARCHAR(2) NULL,
  gender VARCHAR(1) NULL
);
 
INSERT INTO users (name, birth_date, country_code, gender) VALUES
  ('Arnaud', '1975-01-01', 'FR', 'M'),
  ('Maria', '2016-10-10', 'DE', 'F'),
  ('Victoria', '2010-02-11', 'US', 'F');