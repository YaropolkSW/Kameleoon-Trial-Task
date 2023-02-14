CREATE TABLE IF NOT EXISTS quote_table(
    id INT PRIMARY KEY AUTO_INCREMENT,
    quote VARCHAR(500),
    creator INT NOT NULL,
    date_of_creation DATETIME,
    votes INT NOT NULL DEFAULT 0,

    FOREIGN KEY (creator) REFERENCES user_table (id)
);