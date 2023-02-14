DROP TABLE IF EXISTS role_table;

CREATE TABLE IF NOT EXISTS role_table(
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20)
);

INSERT INTO role_table(role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

DROP TABLE IF EXISTS user_table;

CREATE TABLE IF NOT EXISTS user_table(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_of_creation DATE NOT NULL
);

INSERT INTO user_table(username, email, password, date_of_creation)
VALUES ('admin', 'example-email1@gmail.com', '$2a$12$Tnq9VeFgvNZm/DztlN32Ye8f8Fk5/zOaomZnFnP9ms13gHlNMXPom', '2023-02-10'),
       ('user', 'example-email2@gmail.com', '$2a$12$IejoDe0J.AZF5tV5RsV1VOVI.E9yNa9nDm0fW/kZTKecpIniS4e9u', '2023-02-11');

DROP TABLE IF EXISTS quote_table;

CREATE TABLE IF NOT EXISTS quote_table(
    id INT PRIMARY KEY AUTO_INCREMENT,
    quote VARCHAR(500),
    creator INT NOT NULL,
    date_of_creation DATETIME,
    votes INT NOT NULL DEFAULT 0,

    FOREIGN KEY (creator) REFERENCES user_table (id)
);

INSERT INTO quote_table(quote, creator, date_of_creation, votes)
VALUES('Настоящая ответственность бывает только личной.', 1, '2023-02-10 00:00:00.000', 10),
      ('Надо любить жизнь больше, чем смысл жизни.', 2, '2010-02-10 00:00:00.000', 7),
      ('Стремитесь не к успеху, а к ценностям, которые он дает', 1, '2020-02-10 00:00:10.000', 9),
      ('Жизнь - это то, что с тобой происходит, пока ты строишь планы.', 2, '2005-02-10 00:00:00.000', 6),
      ('Начинать всегда стоит с того, что сеет сомнения.', 1, '2015-02-10 00:00:00.000', 8);

DROP TABLE IF EXISTS user_role_table;

CREATE TABLE IF NOT EXISTS user_role_table(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user_table (id),
    FOREIGN KEY (role_id) REFERENCES role_table (id)
);

INSERT INTO user_role_table(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);