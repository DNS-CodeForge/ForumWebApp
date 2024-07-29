-- Create the database and tables
CREATE DATABASE IF NOT EXISTS forum;
USE forum;

CREATE OR REPLACE TABLE forum.`level-info` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    current_exp INT NOT NULL,
    currnet_level INT NOT NULL,
    exp_to_next_level INT NOT NULL
);

CREATE OR REPLACE TABLE forum.roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255) NULL
);

CREATE OR REPLACE TABLE forum.tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE OR REPLACE TABLE forum.users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(32) NULL,
    last_name VARCHAR(32) NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(20) NOT NULL,
    photo_url VARCHAR(255) DEFAULT 'https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' NULL,
    level_info_id INT NULL,
    CONSTRAINT UK_email UNIQUE (email),
    CONSTRAINT UK_username UNIQUE (username),
    CONSTRAINT UKmmvaefewbrvfvqqgohqiyj6qg UNIQUE (level_info_id),
    CONSTRAINT FKaqpyevuifjirnjl2xvqwk0pon FOREIGN KEY (level_info_id) REFERENCES forum.`level-info` (id)
);

CREATE OR REPLACE TABLE forum.phone_numbers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    number VARCHAR(255) NULL,
    CONSTRAINT UK_user_id UNIQUE (user_id),
    CONSTRAINT FK_phone_user FOREIGN KEY (user_id) REFERENCES forum.users (id)
);

CREATE OR REPLACE TABLE forum.posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    created_date DATETIME(6) NULL,
    description VARCHAR(8192) NOT NULL,
    title VARCHAR(64) NOT NULL,
    CONSTRAINT FK_post_user FOREIGN KEY (user_id) REFERENCES forum.users (id)
);

CREATE OR REPLACE TABLE forum.comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    content VARCHAR(1024) NOT NULL,
    user_id INT NULL,
    CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES forum.users (id),
    CONSTRAINT FK_comment_post FOREIGN KEY (post_id) REFERENCES forum.posts (id)
);

CREATE OR REPLACE TABLE forum.likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    user_id INT NULL,
    CONSTRAINT FK_like_post FOREIGN KEY (post_id) REFERENCES forum.posts (id),
    CONSTRAINT FK_like_user FOREIGN KEY (user_id) REFERENCES forum.users (id)
);

CREATE OR REPLACE TABLE forum.post_tags (
    post_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT FK_post_tags_post FOREIGN KEY (post_id) REFERENCES forum.posts (id),
    CONSTRAINT FK_post_tags_tag FOREIGN KEY (tag_id) REFERENCES forum.tags (id)
);

CREATE OR REPLACE TABLE forum.user_role_junction (
    role_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT FK_user_role_junction_role FOREIGN KEY (role_id) REFERENCES forum.roles (role_id),
    CONSTRAINT FK_user_role_junction_user FOREIGN KEY (user_id) REFERENCES forum.users (id)
);

-- Insert mock data into roles
INSERT INTO roles (authority) VALUES ('USER'), ('MODERATOR'), ('ADMIN'), ('BANNED');

-- Insert mock data into tags
INSERT INTO tags (name) VALUES ('Java'), ('Spring'), ('Hibernate'), ('JPA'), ('SQL'),
                               ('Docker'), ('Kubernetes'), ('Microservices'), ('DevOps'),
                               ('Python'), ('JavaScript'), ('TypeScript'), ('React'),
                               ('Angular'), ('Vue'), ('HTML'), ('CSS'), ('NoSQL'),
                               ('Machine Learning'), ('AI');

-- Insert mock data into level-info
INSERT INTO `level-info` (current_exp, currnet_level, exp_to_next_level) VALUES
                                                                             (100, 1, 200), (300, 2, 400), (500, 3, 600), (700, 4, 800), (900, 5, 1000),
                                                                             (1100, 6, 1200), (1300, 7, 1400), (1500, 8, 1600), (1700, 9, 1800), (1900, 10, 2000),
                                                                             (2100, 11, 2200), (2300, 12, 2400), (2500, 13, 2600), (2700, 14, 2800), (2900, 15, 3000),
                                                                             (3100, 16, 3200), (3300, 17, 3400), (3500, 18, 3600), (3700, 19, 3800), (3900, 20, 4000);

-- Insert mock data into users
INSERT INTO users (first_name, last_name, email, password, username, level_info_id)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'johndoe', 1),
    ('Brad', 'Doe', 'brad.doe@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'braddoe', 2),
    ('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'janesmith', 3),
    ('Alice', 'Johnson', 'alice.johnson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'alicejohnson', 4),
    ('Bob', 'Brown', 'bob.brown@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'bobbrown', 5),
    ('Charlie', 'Davis', 'charlie.davis@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'charliedavis', 6),
    ('David', 'Wilson', 'david.wilson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'davidwilson', 7),
    ('Eva', 'Moore', 'eva.moore@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'evamoore', 8),
    ('Frank', 'Taylor', 'frank.taylor@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'franktaylor', 9),
    ('Grace', 'Anderson', 'grace.anderson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'graceanderson', 10),
    ('Hank', 'Thomas', 'hank.thomas@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'hankthomas', 11),
    ('Ivy', 'Jackson', 'ivy.jackson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'ivyjackson', 12),
    ('Jack', 'White', 'jack.white@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'jackwhite', 13),
    ('Kathy', 'Harris', 'kathy.harris@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'kathyharris', 14),
    ('Leo', 'Martin', 'leo.martin@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'leomartin', 15),
    ('Mia', 'Clark', 'mia.clark@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'miaclark', 16),
    ('Nina', 'Lewis', 'nina.lewis@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'ninalewis', 17),
    ('Oscar', 'Walker', 'oscar.walker@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'oscarwalker', 18),
    ('Paul', 'Hall', 'paul.hall@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'paulhall', 19),
    ('Quinn', 'Allen', 'quinn.allen@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'quinnallen', 20);

-- Assign roles to users
INSERT INTO user_role_junction (role_id, user_id)
VALUES
    (1, 1), (4, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
    (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18),
    (1, 19), (1, 20);

-- Insert mock data into phone_numbers for admin only (user_id 1 is reserved for admin)
INSERT INTO phone_numbers (user_id, number)
VALUES (1, '123-456-7890');

-- Insert mock data into posts
INSERT INTO posts (user_id, created_date, description, title)
VALUES
    (1, NOW(), 'Exploring the features of Java 16.', 'Java 16 Features'),
    (2, NOW(), 'Spring Boot makes microservices development easy.', 'Spring Boot Microservices'),
    (3, NOW(), 'Hibernate ORM and its advantages.', 'Understanding Hibernate ORM'),
    (4, NOW(), 'JPA for data persistence in Java applications.', 'JPA and Data Persistence'),
    (5, NOW(), 'SQL vs NoSQL: When to use which?', 'SQL vs NoSQL'),
    (6, NOW(), 'Docker containers for application deployment.', 'Deploying with Docker'),
    (7, NOW(), 'Kubernetes for container orchestration.', 'Kubernetes Basics'),
    (8, NOW(), 'Building scalable applications with Microservices.', 'Scalable Microservices'),
    (9, NOW(), 'DevOps practices and tools.', 'Introduction to DevOps'),
    (10, NOW(), 'Python for data analysis and machine learning.', 'Python for Data Analysis'),
    (11, NOW(), 'Modern JavaScript frameworks.', 'Exploring JavaScript Frameworks'),
    (12, NOW(), 'TypeScript for better JavaScript development.', 'Why TypeScript?'),
    (13, NOW(), 'React for building dynamic user interfaces.', 'Getting Started with React'),
    (14, NOW(), 'Angular for enterprise applications.', 'Building with Angular'),
    (15, NOW(), 'Vue.js for modern web development.', 'Vue.js Essentials'),
    (16, NOW(), 'HTML and CSS for web design.', 'Web Design Basics'),
    (17, NOW(), 'NoSQL databases for high performance.', 'Introduction to NoSQL'),
    (18, NOW(), 'Machine Learning fundamentals.', 'Machine Learning 101'),
    (19, NOW(), 'AI and its impact on technology.', 'The Future of AI'),
    (20, NOW(), 'Advanced SQL queries for data analysis.', 'Mastering SQL Queries');

-- Insert mock data into comments
INSERT INTO comments (post_id, content, user_id)
VALUES
    (1, 'Great overview of Java 16!', 2), (1, 'Very informative.', 3), (2, 'Spring Boot is awesome!', 4),
    (3, 'Helpful article on Hibernate.', 5), (4, 'Good insights on JPA.', 6), (5, 'Nice comparison!', 7),
    (6, 'Docker is a game changer.', 8), (7, 'Kubernetes is powerful!', 9), (8, 'Scalability is key.', 10),
    (9, 'DevOps practices are essential.', 11), (10, 'Python is great for data analysis.', 12),
    (11, 'JavaScript frameworks are evolving fast.', 13), (12, 'TypeScript improves productivity.', 14),
    (13, 'React is my favorite UI library.', 15), (14, 'Angular is robust for large apps.', 16),
    (15, 'Vue.js is very flexible.', 17), (16, 'HTML and CSS are foundational.', 18),
    (17, 'NoSQL databases offer great performance.', 19), (18, 'Machine Learning is fascinating.', 20),
    (19, 'AI will shape the future.', 2), (20, 'SQL queries can be very powerful.', 3);

-- Insert mock data into likes
INSERT INTO likes (post_id, user_id)
VALUES
    (1, 2), (1, 3), (2, 4), (2, 5), (3, 6), (3, 7),
    (4, 8), (4, 9), (5, 10), (5, 11), (6, 12), (6, 13),
    (7, 14), (7, 15), (8, 16), (8, 17), (9, 18), (9, 19),
    (10, 20), (10, 2), (11, 3), (11, 4), (12, 5), (12, 6),
    (13, 7), (13, 8), (14, 9), (14, 10), (15, 11), (15, 12),
    (16, 13), (16, 14), (17, 15), (17, 16), (18, 17), (18, 18),
    (19, 19), (19, 20), (20, 2), (20, 3);

-- Insert mock data into post_tags
INSERT INTO post_tags (post_id, tag_id)
VALUES
    (1, 1), (1, 2), (2, 3), (2, 4), (3, 5), (3, 6),
    (4, 7), (4, 8), (5, 9), (5, 10), (6, 11), (6, 12),
    (7, 13), (7, 14), (8, 15), (8, 16), (9, 17), (9, 18),
    (10, 19), (10, 20), (11, 1), (11, 2), (12, 3), (12, 4),
    (13, 5), (13, 6), (14, 7), (14, 8), (15, 9), (15, 10),
    (16, 11), (16, 12), (17, 13), (17, 14), (18, 15), (18, 16),
    (19, 17), (19, 18), (20, 19), (20, 20);
