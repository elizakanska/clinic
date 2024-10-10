USE clinic;

-- Drop existing tables, ensuring the foreign key constraint is removed
ALTER TABLE authorities DROP FOREIGN KEY authorities_fk;
DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

-- Create Users table with an additional 'specialty' field for vets
CREATE TABLE users (
                       username varchar(50) NOT NULL,
                       password char(68) NOT NULL,
                       email varchar(100) NOT NULL,
                       enabled tinyint(1) NOT NULL,
                       specialty varchar(100),  -- This field will only be used for vets, can be NULL for other users
                       PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Authorities table to manage user roles (admin, owner, vet)
CREATE TABLE authorities (
                             username varchar(50) NOT NULL,
                             authority varchar(50) NOT NULL,
                             UNIQUE KEY authorities_idx (username, authority),
                             CONSTRAINT authorities_fk FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert example users (admin, owners, vet with a specialty)
INSERT INTO users(username, password, email, enabled, specialty) VALUES
                                                                     ('admin','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa', 'admin@clinic.com', 1, NULL),
                                                                     ('jane','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa', 'jane@owner.com', 1, NULL),
                                                                     ('john','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa', 'john@owner.com', 1, NULL),
                                                                     ('vetuser','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa', 'vet@clinic.com', 1, 'Surgery');

-- Assign roles (admin, owner, vet)
INSERT INTO authorities (username, authority) VALUES
                                                  ('admin', 'ROLE_ADMIN'),
                                                  ('jane', 'ROLE_OWNER'),
                                                  ('john', 'ROLE_OWNER'),
                                                  ('vetuser', 'ROLE_VET');

-- Create Pets table, linking to the users (owners)
CREATE TABLE pet (
                     pet_id bigint(20) NOT NULL AUTO_INCREMENT,
                     owner_username varchar(50) NOT NULL,
                     name varchar(45) NOT NULL,
                     species varchar(45) NOT NULL,
                     type varchar(45) NOT NULL,
                     age bigint(20) NOT NULL,
                     notes varchar(255),
                     PRIMARY KEY (pet_id),
                     CONSTRAINT pet_owner_fk FOREIGN KEY (owner_username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Visits table, linking pets to vets (only those with ROLE_VET)
CREATE TABLE visit (
                       visit_id bigint(20) NOT NULL AUTO_INCREMENT,
                       pet_id bigint(20) NOT NULL,
                       vet_username varchar(50) NOT NULL,
                       time datetime NOT NULL,
                       is_payed tinyint(1),
                       link varchar(255) NOT NULL,
                       PRIMARY KEY (visit_id),
                       CONSTRAINT visit_pet_fk FOREIGN KEY (pet_id) REFERENCES pet (pet_id),
                       CONSTRAINT visit_vet_fk FOREIGN KEY (vet_username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
