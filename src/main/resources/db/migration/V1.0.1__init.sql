USE clinic;

ALTER TABLE visit
    DROP FOREIGN KEY FK_pet_id;

DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS visit;

CREATE TABLE pet (
    pet_id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    species varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    type varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    age bigint(20) NOT NULL,
    notes varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE visit (
    visit_id bigint(20) NOT NULL AUTO_INCREMENT,
    pet_id bigint(20) NOT NULL,
    doc_id bigint(20) NOT NULL,
    time datetime NOT NULL,
    is_payed tinyint(1),
    link varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (visit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE visit
    ADD CONSTRAINT FK_pet_id FOREIGN KEY (pet_id)
        REFERENCES pet(pet_id);
