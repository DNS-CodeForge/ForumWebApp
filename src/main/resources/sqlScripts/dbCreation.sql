CREATE DATABASE IF NOT EXISTS forum;

USE forum;

CREATE OR REPLACE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255) NULL
);

CREATE OR REPLACE TABLE tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NULL
);

CREATE OR REPLACE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(32) NULL,
    last_name VARCHAR(32) NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT UK_email UNIQUE (email),
    CONSTRAINT UK_username UNIQUE (username)
);

CREATE OR REPLACE TABLE phone_numbers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    number VARCHAR(255) NULL,
    CONSTRAINT UK_user_id UNIQUE (user_id),
    CONSTRAINT FK_phone_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE OR REPLACE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    created_date DATETIME(6) NULL,
    description VARCHAR(255) NULL,
    title VARCHAR(255) NULL,
    CONSTRAINT FK_post_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE OR REPLACE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    content VARCHAR(255) NULL,
    CONSTRAINT FK_comment_post FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE OR REPLACE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    user_id INT NULL,
    CONSTRAINT FK_like_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_like_post FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE OR REPLACE TABLE post_tags (
    post_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT FK_post_tags_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT FK_post_tags_tag FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE OR REPLACE TABLE user_role_junction (
    role_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT FK_user_role_junction_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_user_role_junction_role FOREIGN KEY (role_id) REFERENCES roles (role_id)
);
