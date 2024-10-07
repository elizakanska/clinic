USE clinic;

DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
    `username` varchar(50) NOT NULL,
    `password` char(68) NOT NULL,
    `enabled` tinyint NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO users(username, password, enabled) VALUES
    ('jane','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa',1),
    ('john','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa',1),
    ('doc','{bcrypt}$2a$12$VJKzDuVYceWHdzrxsWk.fO4IBy3RjVPXhFXggho1YZ9f1XHpkhzBa',1);
#password

CREATE TABLE `authorities` (
    `username` varchar(50) NOT NULL,
    `authority` varchar(50) NOT NULL,
    UNIQUE KEY `authorities4_idx_1` (`username`,`authority`),
    CONSTRAINT `authorities4_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO authorities (username, authority) VALUES
    ('jane', 'ROLE_OWNER'),
    ('john', 'ROLE_OWNER'),
    ('doc', 'ROLE_OWNER'),
    ('doc', 'ROLE_VET');