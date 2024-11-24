USE clinic;

-- Drop tables (automatically drops associated foreign keys)
DROP TABLE IF EXISTS visit, pet, authorities, users;

-- Create Users table
CREATE TABLE users (
                       username VARCHAR(50) NOT NULL,
                       password CHAR(68) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       enabled TINYINT(1) NOT NULL,
                       specialty VARCHAR(100), -- NULL for non-vets, used only for vets
                       PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Authorities table for user roles (admin, user, vet)
CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             UNIQUE KEY (username, authority),
                             CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Pet table linked to users (owners)
CREATE TABLE pet (
                     pet_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                     owner_username VARCHAR(50) NOT NULL,
                     name VARCHAR(45) NOT NULL,
                     species VARCHAR(45) NOT NULL,
                     type VARCHAR(45) NOT NULL,
                     age BIGINT(20) NOT NULL,
                     notes VARCHAR(255),
                     PRIMARY KEY (pet_id),
                     CONSTRAINT fk_pet_owner FOREIGN KEY (owner_username) REFERENCES users(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Visit table linked to pets and vets
CREATE TABLE visit (
                       visit_id BIGINT(20) NOT NULL AUTO_INCREMENT,
                       pet_id BIGINT(20) NOT NULL,
                       vet_username VARCHAR(50) NOT NULL,
                       time DATETIME NOT NULL,
                       is_payed TINYINT(1),
                       link VARCHAR(255),
                       PRIMARY KEY (visit_id),
                       CONSTRAINT fk_visit_pet FOREIGN KEY (pet_id) REFERENCES pet(pet_id),
                       CONSTRAINT fk_visit_vet FOREIGN KEY (vet_username) REFERENCES users(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert example users
INSERT INTO users (username, password, email, enabled, specialty) VALUES
                                                                      ('admin', '{bcrypt}$2a$12$VJKzDuVY...', 'admin@clinic.com', 1, NULL),
                                                                      ('jane', '{bcrypt}$2a$12$VJKzDuVY...', 'jane@owner.com', 1, NULL),
                                                                      ('john', '{bcrypt}$2a$12$VJKzDuVY...', 'john@owner.com', 1, NULL),
                                                                      ('vetuser', '{bcrypt}$2a$12$VJKzDuVY...', 'vet@clinic.com', 1, 'Surgery');

-- Assign roles
INSERT INTO authorities (username, authority) VALUES
                                                  ('admin', 'ROLE_ADMIN'),
                                                  ('jane', 'ROLE_USER'),
                                                  ('john', 'ROLE_USER'),
                                                  ('vetuser', 'ROLE_VET');

-- Temporarily drop the foreign key constraint
ALTER TABLE visit DROP FOREIGN KEY fk_visit_vet;

-- Re-add the foreign key constraint
ALTER TABLE visit ADD CONSTRAINT fk_visit_vet FOREIGN KEY (vet_username) REFERENCES users(username);
