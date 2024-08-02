SET
FOREIGN_KEY_CHECKS = 0;
-- Create tables
CREATE
OR REPLACE TABLE SPRING_SESSION
(
    PRIMARY_ID            CHAR(36)     NOT NULL PRIMARY KEY,
    SESSION_ID            CHAR(36)     NOT NULL,
    CREATION_TIME         BIGINT       NOT NULL,
    LAST_ACCESS_TIME      BIGINT       NOT NULL,
    MAX_INACTIVE_INTERVAL INT          NOT NULL,
    EXPIRY_TIME           BIGINT       NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100) NULL,
    CONSTRAINT SPRING_SESSION_IX1 UNIQUE (SESSION_ID)
)
    ROW_FORMAT = DYNAMIC;

CREATE
OR REPLACE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE
OR REPLACE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE
OR REPLACE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BLOB         NOT NULL,
    PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
)
    ROW_FORMAT = DYNAMIC;

CREATE
OR REPLACE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);

CREATE
OR REPLACE TABLE level_info
(
    current_exp       INT NOT NULL,
    current_level     INT NOT NULL,
    exp_to_next_level INT NOT NULL,
    user_id           INT NOT NULL PRIMARY KEY
);

CREATE
OR REPLACE TABLE roles
(
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255) NULL
);

CREATE
OR REPLACE TABLE tags
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE
OR REPLACE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(20)  NOT NULL,
    first_name VARCHAR(32)  NULL,
    last_name  VARCHAR(32)  NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    photo_url  VARCHAR(255) NOT NULL,
    CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT UKr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
);

CREATE
OR REPLACE TABLE phone_numbers
(
    user_id INT          NOT NULL PRIMARY KEY,
    number  VARCHAR(255) NULL,
    CONSTRAINT FKg077extnnxwv904qjw2kwinpg FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE
OR REPLACE TABLE posts
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT           NULL,
    created_date DATETIME(6)   NULL,
    title        VARCHAR(64)   NOT NULL,
    description  VARCHAR(8192) NOT NULL,
    CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE
OR REPLACE TABLE comments
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT           NOT NULL,
    user_id INT           NULL,
    content VARCHAR(1024) NOT NULL,
    CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKh4c7lvsc298whoyd4w9ta25cr FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE
OR REPLACE TABLE likes
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    user_id INT NULL,
    CONSTRAINT FKnvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKry8tnr4x2vwemv2bb0h5hyl0x FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE
OR REPLACE TABLE post_tags
(
    post_id INT NOT NULL,
    tag_id  INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT FKkifam22p4s1nm3bkmp1igcn5w FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT FKm6cfovkyqvu5rlm6ahdx3eavj FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE
OR REPLACE TABLE user_role_junction
(
    role_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT FK5aqfsa7i8mxrr51gtbpcvp0v1 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKhybpcwvq8snjhbxawo25hxous FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

-- Insert roles
INSERT INTO roles (role_id, authority)
VALUES (1, 'USER'),
       (2, 'MODERATOR'),
       (3, 'ADMIN'),
       (4, 'BANNED');


-- Insert admin user
INSERT INTO users (id, username, first_name, last_name, email, password, photo_url)
VALUES (1, 'admin', 'Admin', 'Admin', 'admin@admin.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly',
        'NoPhoto');

-- Insert admin role for admin user
INSERT INTO user_role_junction (role_id, user_id)
VALUES (3, 1),
       (1, 1);

-- Insert other users
INSERT INTO users (username, first_name, last_name, email, password, photo_url)
VALUES ('jdoe', 'John', 'Doe', 'johndoe@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('asmith', 'Alice', 'Smith', 'alicesmith@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('bwilliams', 'Bob', 'Williams', 'bobwilliams@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('cjohnson', 'Carol', 'Johnson', 'caroljohnson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('dwhite', 'David', 'White', 'davidwhite@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('emiller', 'Eva', 'Miller', 'evamiller@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('fjackson', 'Frank', 'Jackson', 'frankjackson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('gthompson', 'Grace', 'Thompson', 'gracethompson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('hgarcia', 'Hank', 'Garcia', 'hankgarcia@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto'),
       ('banneduser', 'Banned', 'User', 'banneduser@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto');

-- Insert roles for other users
INSERT INTO user_role_junction (role_id, user_id)
VALUES (1, 2),  -- USER role
       (1, 3),  -- USER role
       (1, 4),  -- USER role
       (1, 5),  -- USER role
       (1, 6),  -- USER role
       (2, 7),  -- USER role
       (1, 8),  -- USER role
       (1, 9),  -- USER role
       (1, 10), -- USER role
       (4, 11);-- BANNED role

-- Insert posts
INSERT INTO posts (user_id, created_date, title, description)
VALUES (2, NOW(), 'Understanding Java Streams',
        'Let''s dive deep into Java Streams and see how we can make the most of it for efficient data processing.'),
       (3, NOW(), 'Introduction to Spring Boot',
        'Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Let''s see how to get started.'),
       (4, NOW(), 'Top 10 VSCode Extensions',
        'Here are my top 10 Visual Studio Code extensions that can boost your productivity.'),
       (5, NOW(), 'Microservices Architecture',
        'Microservices architecture is all the rage these days. Let''s discuss the benefits and challenges.'),
       (6, NOW(), 'Understanding Docker',
        'Docker allows you to package an application with all of its dependencies into a standardized unit. Let''s explore.'),
       (7, NOW(), 'REST vs GraphQL', 'Which one should you use? REST or GraphQL? Let''s compare the two.'),
       (8, NOW(), 'Getting Started with Kubernetes',
        'Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.'),
       (9, NOW(), 'Mastering Python Decorators',
        'Decorators provide a simple syntax for calling higher-order functions in Python. Let''s master them.'),
       (10, NOW(), 'Exploring React Hooks',
        'Hooks are a new addition in React that let you use state and other React features without writing a class.'),
       (11, NOW(), 'Why I Got Banned', 'Here is the story of how I got banned from this forum.');

-- Insert comments
INSERT INTO comments (post_id, user_id, content)
VALUES (1, 3, 'Great post, John! Java Streams can indeed make data processing very efficient.'),
       (2, 4, 'Thanks for the introduction to Spring Boot, Alice. It seems very powerful.'),
       (3, 5, 'I love using VSCode and these extensions are a must-have. Thanks for sharing, Bob!'),
       (4, 6, 'Carol, Microservices are great but they come with their own set of challenges.'),
       (5, 7, 'David, Docker is indeed a game-changer in the DevOps world.'),
       (6, 8, 'Eva, I prefer GraphQL for its flexibility, but REST is still very robust.'),
       (7, 9, 'Frank, Kubernetes seems complex but your post made it easier to understand. Thanks!'),
       (8, 10, 'Grace, Python decorators are a bit tricky at first but very useful once you get the hang of it.'),
       (9, 2, 'Hank, React Hooks have completely changed the way I write React components. Great post!'),
       (10, 3, 'Banned User, it''s unfortunate you got banned. Let''s see what happened.');

-- Insert likes
INSERT INTO likes (post_id, user_id)
VALUES (1, 3),
       (2, 4),
       (3, 5),
       (4, 6),
       (5, 7),
       (6, 8),
       (7, 9),
       (8, 10),
       (9, 2),
       (10, 3);

-- Insert post tags
INSERT INTO tags (name)
VALUES ('Java'),
       ('Spring Boot'),
       ('VSCode'),
       ('Microservices'),
       ('Docker'),
       ('REST'),
       ('GraphQL'),
       ('Kubernetes'),
       ('Python'),
       ('React');
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1), -- Java
       (2, 2), -- Spring Boot
       (3, 3), -- VSCode
       (4, 4), -- Microservices
       (5, 5), -- Docker
       (6, 6), -- REST
       (6, 7), -- GraphQL
       (7, 8), -- Kubernetes
       (8, 9), -- Python
       (9, 10);-- React

-- Reset foreign key checks
               INSERT INTO level_info VALUES
SET
FOREIGN_KEY_CHECKS = 1;
