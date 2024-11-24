-- Insert pets
INSERT INTO pet (owner_username, name, species, type, age, notes) VALUES
    ('jane', 'Bella', 'Suns', 'Labradora', 3, 'Ļoti spēlīgs'),
    ('jane', 'Mia', 'Kaķis', 'Siamiešu', 2, 'Kaunīgs, bet draudzīgs'),
    ('john', 'Max', 'Suns', 'Buldogs', 5, 'Mīl košļāt rotaļlietas'),
    ('john', 'Zeltainā', 'Zivs', 'Zeltzivs', 1, 'Peld visu dienu'),
    ('john', 'Čiriks', 'Putns', 'Kanārijputns', 2, 'Skaisti dzied');

-- Insert visits
INSERT INTO visit (pet_id, vet_username, time, is_payed, link) VALUES
    (1, 'vetuser', '2024-10-01 09:00:00', 1, 'http://clinic.com/visit/1'),
    (2, 'vetuser', '2024-10-02 10:30:00', 0, ''),
    (3, 'vetuser', '2024-10-03 12:00:00', 1, 'http://clinic.com/visit/3'),
    (4, 'vetuser', '2024-10-04 14:00:00', 0, ''),
    (5, 'vetuser', '2024-10-05 15:30:00', 1, 'http://clinic.com/visit/5');
