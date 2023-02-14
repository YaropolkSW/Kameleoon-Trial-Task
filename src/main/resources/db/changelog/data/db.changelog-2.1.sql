CREATE TABLE IF NOT EXISTS user_table(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_of_creation DATE NOT NULL
);