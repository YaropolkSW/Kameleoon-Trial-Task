CREATE TABLE IF NOT EXISTS user_role_table(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user_table (id),
    FOREIGN KEY (role_id) REFERENCES role_table (id)
);