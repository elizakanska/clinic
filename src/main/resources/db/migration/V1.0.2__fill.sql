USE clinic;

INSERT INTO pet (name, species, age) VALUES
    ('Buddy', 'Dog', 3),
    ('Whiskers', 'Cat', 2),
    ('Rocky', 'Dog', 5),
    ('Max', 'Dog', 4),
    ('Oliver', 'Cat', 3),
    ('Luna', 'Dog', 2),
    ('Charlie', 'Cat', 4),
    ('Daisy', 'Parrot', 1),
    ('Milo', 'Parrot', 5),
    ('Titanium Maximus', 'Cat', 1);

ALTER TABLE visit AUTO_INCREMENT = 1;

INSERT INTO visit (pet_id, time, reason) VALUES
    (1, '2024-02-29 09:00:00', 'Regular checkup'),
    (2, '2024-02-29 09:30:00', 'Vaccination'),
    (3, '2024-02-29 10:00:00', 'Dental cleaning'),
    (4, '2024-02-29 10:30:00', 'Sick visit'),
    (5, '2024-02-29 11:00:00', 'Annual exam'),
    (6, '2024-02-29 11:30:00', 'Spaying/neutering'),
    (7, '2024-02-29 12:00:00', 'Injury treatment'),
    (8, '2024-02-29 12:30:00', 'Flea prevention'),
    (9, '2024-02-29 13:00:00', 'Bird checkup'),
    (10, '2024-02-29 13:30:00', 'Behavioral consultation');
