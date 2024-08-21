SET FOREIGN_KEY_CHECKS = 0;

-- Create tables
CREATE OR REPLACE TABLE SPRING_SESSION (
    PRIMARY_ID            CHAR(36)     NOT NULL PRIMARY KEY,
    SESSION_ID            CHAR(36)     NOT NULL,
    CREATION_TIME         BIGINT       NOT NULL,
    LAST_ACCESS_TIME      BIGINT       NOT NULL,
    MAX_INACTIVE_INTERVAL INT          NOT NULL,
    EXPIRY_TIME           BIGINT       NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100) NULL,
    CONSTRAINT SPRING_SESSION_IX1 UNIQUE (SESSION_ID)
) ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE OR REPLACE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE OR REPLACE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BLOB         NOT NULL,
    PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
) ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);

CREATE OR REPLACE TABLE level_info (
    current_exp       INT NOT NULL,
    current_level     INT NOT NULL,
    exp_to_next_level INT NOT NULL,
    user_id           INT NOT NULL PRIMARY KEY
);

CREATE OR REPLACE TABLE roles (
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255) NULL
);

CREATE OR REPLACE TABLE tags (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE OR REPLACE TABLE users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(20)  NOT NULL,
    first_name VARCHAR(32)  NULL,
    last_name  VARCHAR(32)  NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    photo_url  VARCHAR(255),
    CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT UKr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
);

CREATE OR REPLACE TABLE phone_numbers (
    user_id INT          NOT NULL PRIMARY KEY,
    number  VARCHAR(255) NULL,
    CONSTRAINT FKg077extnnxwv904qjw2kwinpg FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE OR REPLACE TABLE posts (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT           NULL,
    created_date DATETIME(6)   NULL,
    title        VARCHAR(64)   NOT NULL,
    description  VARCHAR(8192) NOT NULL,
    CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE OR REPLACE TABLE comments (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT           NOT NULL,
    user_id INT           NULL,
    content VARCHAR(1024) NOT NULL,
    CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKh4c7lvsc298whoyd4w9ta25cr FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE OR REPLACE TABLE likes (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NULL,
    user_id INT NULL,
    CONSTRAINT FKnvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKry8tnr4x2vwemv2bb0h5hyl0x FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE OR REPLACE TABLE post_tags (
    post_id INT NOT NULL,
    tag_id  INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT FKkifam22p4s1nm3bkmp1igcn5w FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT FKm6cfovkyqvu5rlm6ahdx3eavj FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE OR REPLACE TABLE user_role_junction (
    role_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT FK5aqfsa7i8mxrr51gtbpcvp0v1 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKhybpcwvq8snjhbxawo25hxous FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

-- Insert roles
INSERT INTO roles (role_id, authority)
VALUES
    (1, 'USER'),
    (2, 'MOD'),
    (3, 'ADMIN'),
    (4, 'BANNED');

-- Insert admin user
INSERT INTO users (id, username, first_name, last_name, email, password, photo_url)
VALUES
    (1, 'admin', 'Admin', 'Admin', 'admin@admin.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png');

-- Insert admin role for admin user
INSERT INTO user_role_junction (role_id, user_id)
VALUES
    (3, 1),
    (1, 1);

-- Insert other users
INSERT INTO users (username, first_name, last_name, email, password, photo_url)
VALUES
    ('jdoe', 'John', 'Doe', 'johndoe@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('asmith', 'Alice', 'Smith', 'alicesmith@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('bwilliams', 'Bob', 'Williams', 'bobwilliams@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('cjohnson', 'Carol', 'Johnson', 'caroljohnson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('dwhite', 'David', 'White', 'davidwhite@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('emiller', 'Eva', 'Miller', 'evamiller@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('fjackson', 'Frank', 'Jackson', 'frankjackson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('gthompson', 'Grace', 'Thompson', 'gracethompson@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('hgarcia', 'Hank', 'Garcia', 'hankgarcia@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('banneduser', 'Banned', 'User', 'banneduser@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('ijones', 'Isaac', 'Jones', 'isaacjones@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('klucas', 'Karen', 'Lucas', 'karenlucas@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('mramos', 'Michael', 'Ramos', 'michaelramos@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('nmartin', 'Nina', 'Martin', 'ninamartin@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('opatton', 'Olivia', 'Patton', 'oliviapatton@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('pbrown', 'Paul', 'Brown', 'paulbrown@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('qgreen', 'Quincy', 'Green', 'quincygreen@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('rking', 'Rachel', 'King', 'rachelking@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('slopez', 'Samuel', 'Lopez', 'samuellopez@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('tallen', 'Theresa', 'Allen', 'theresaallen@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('ubaker', 'Ulysses', 'Baker', 'ulyssesbaker@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('vcarter', 'Valerie', 'Carter', 'valeriecarter@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('wbell', 'Walter', 'Bell', 'walterbell@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('xmoore', 'Xander', 'Moore', 'xandermoore@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('ybryant', 'Yasmin', 'Bryant', 'yasminbryant@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('zpowell', 'Zane', 'Powell', 'zanepowell@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('jgreen', 'John', 'Green', 'johngreen@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png'),
    ('kblack', 'Kevin', 'Black', 'kevinblack@example.com', '$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', '/images/profile/NoPhoto.png');

-- Insert roles for other users
INSERT INTO user_role_junction (role_id, user_id)
VALUES
    (1, 2),  -- USER role
    (1, 3),  -- USER role
    (1, 4),  -- USER role
    (1, 5),  -- USER role
    (1, 6),  -- USER role
    (2, 7),  -- MOD role
    (1, 8),  -- USER role
    (1, 9),  -- USER role
    (1, 10), -- USER role
    (4, 11), -- BANNED role
    (1, 11), -- USER role
    (1, 12), -- USER role
    (1, 13), -- USER role
    (1, 14), -- USER role
    (1, 15), -- USER role
    (1, 16), -- USER role
    (1, 17), -- USER role
    (1, 18), -- USER role
    (1, 19), -- USER role
    (1, 20), -- USER role
    (1, 21), -- USER role
    (1, 22), -- USER role
    (1, 23), -- USER role
    (1, 24), -- USER role
    (1, 25), -- USER role
    (1, 26), -- USER role
    (4, 27), -- BANNED role
    (1, 27), -- USER role
    (4, 28), -- BANNED role
    (1, 28); -- USER role

-- Insert posts
INSERT INTO posts (user_id, created_date, title, description)
VALUES
    (2, NOW(), 'Understanding Java Streams', 'Let''s dive deep into Java Streams and see how we can make the most of it for efficient data processing.'),
    (3, NOW(), 'Introduction to Spring Boot', 'Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Let''s see how to get started.'),
    (4, NOW(), 'Top 10 VSCode Extensions', 'Here are my top 10 Visual Studio Code extensions that can boost your productivity.'),
    (5, NOW(), 'Microservices Architecture', 'Microservices architecture is all the rage these days. Let''s discuss the benefits and challenges.'),
    (6, NOW(), 'Understanding Docker', 'Docker allows you to package an application with all of its dependencies into a standardized unit. Let''s explore.'),
    (7, NOW(), 'REST vs GraphQL', 'Which one should you use? REST or GraphQL? Let''s compare the two.'),
    (8, NOW(), 'Getting Started with Kubernetes', 'Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.'),
    (9, NOW(), 'Mastering Python Decorators', 'Decorators provide a simple syntax for calling higher-order functions in Python. Let''s master them.'),
    (10, NOW(), 'Exploring React Hooks', 'Hooks are a new addition in React that let you use state and other React features without writing a class.'),
    (11, NOW(), 'Why I Got Banned', 'Here is the story of how I got banned from this forum.'),
    (12, NOW(), 'Exploring AI in Python', 'A comprehensive guide to implementing AI solutions using Python.'),
    (13, NOW(), 'Best Practices for Microservices', 'Let’s explore the best practices when working with microservices architecture.'),
    (14, NOW(), 'Intro to Machine Learning', 'An introductory post on getting started with machine learning using Python.'),
    (15, NOW(), 'Advanced Docker Techniques', 'Deep dive into advanced Docker techniques and best practices.'),
    (16, NOW(), 'The Power of GraphQL', 'Understanding the benefits of using GraphQL in modern applications.'),
    (17, NOW(), 'Kubernetes: Beyond the Basics', 'A look into advanced Kubernetes features and how to utilize them.'),
    (18, NOW(), 'Top 5 CI/CD Tools', 'An overview of the top 5 CI/CD tools available for modern software development.'),
    (19, NOW(), 'React Native vs Flutter', 'Comparing React Native and Flutter for mobile app development.'),
    (20, NOW(), 'AI Ethics in Tech', 'Discussing the importance of ethics in AI and how it impacts technology.'),
    (21, NOW(), 'Why Choose MongoDB?', 'Exploring the features that make MongoDB a popular choice for developers.'),
    (22, NOW(), 'Getting Started with TypeScript', 'An introductory guide to TypeScript for JavaScript developers.'),
    (23, NOW(), 'Understanding OAuth 2.0', 'A deep dive into OAuth 2.0 and how it secures modern web applications.'),
    (24, NOW(), 'Introduction to AWS Lambda', 'Getting started with serverless computing using AWS Lambda.'),
    (25, NOW(), 'Demystifying Kubernetes Networking', 'Understanding networking within a Kubernetes cluster.'),
    (26, NOW(), 'Optimizing Web Performance', 'Tips and tricks to optimize the performance of your web applications.'),
    (27, NOW(), 'Best Practices for RESTful APIs', 'A guide to designing and implementing RESTful APIs.'),
    (28, NOW(), 'Java Concurrency in Practice', 'A look at concurrency in Java and how to handle it effectively.'),
    (29, NOW(), 'Comparing NoSQL and SQL Databases', 'Understanding the differences and when to use each database type.'),
    (30, NOW(), 'Building Progressive Web Apps', 'An introduction to Progressive Web Apps (PWAs) and how to build them.');

-- Insert comments
INSERT INTO comments (post_id, user_id, content)
VALUES
    (1, 3, 'Great post, John! Java Streams can indeed make data processing very efficient.'),
    (2, 4, 'Thanks for the introduction to Spring Boot, Alice. It seems very powerful.'),
    (3, 5, 'I love using VSCode and these extensions are a must-have. Thanks for sharing, Bob!'),
    (4, 6, 'Carol, Microservices are great but they come with their own set of challenges.'),
    (5, 7, 'David, Docker is indeed a game-changer in the DevOps world.'),
    (6, 8, 'Eva, I prefer GraphQL for its flexibility, but REST is still very robust.'),
    (7, 9, 'Frank, Kubernetes seems complex but your post made it easier to understand. Thanks!'),
    (8, 10, 'Grace, Python decorators are a bit tricky at first but very useful once you get the hang of it.'),
    (9, 2, 'Hank, React Hooks have completely changed the way I write React components. Great post!'),
    (10, 3, 'Banned User, it''s unfortunate you got banned. Let''s see what happened.'),
    (12, 13, 'Python is indeed very powerful for AI development. Thanks for sharing!'),
    (13, 14, 'Microservices can be tricky, but these practices help a lot.'),
    (14, 15, 'Machine learning is the future. Great introduction!'),
    (15, 16, 'Docker continues to impress me with its capabilities.'),
    (16, 17, 'GraphQL has changed the way we interact with APIs.'),
    (17, 18, 'Kubernetes can be complex, but mastering it is rewarding.'),
    (18, 19, 'CI/CD is essential in modern development, great tools!'),
    (19, 20, 'React Native is more mature, but Flutter is catching up fast.'),
    (20, 21, 'AI ethics is a crucial discussion that needs more attention.'),
    (21, 22, 'MongoDB’s flexibility is what makes it a go-to database.'),
    (22, 23, 'TypeScript has made JavaScript development so much easier.'),
    (23, 24, 'OAuth 2.0 is a bit complex, but this post made it clearer.'),
    (24, 25, 'Serverless is the future, AWS Lambda is a great tool.'),
    (25, 26, 'Kubernetes networking is one of the trickiest parts, thanks for the insight.'),
    (26, 27, 'Web performance is often overlooked but so important.'),
    (27, 28, 'RESTful APIs are still the backbone of the web.'),
    (28, 29, 'Concurrency in Java is a critical topic, thanks for addressing it.'),
    (29, 30, 'Both NoSQL and SQL have their places, great comparison!'),
    (30, 12, 'PWAs are the future of web development, thanks for the guide!');

-- Insert likes
INSERT INTO likes (post_id, user_id)
VALUES
    (1, 3),
    (2, 4),
    (3, 5),
    (4, 6),
    (5, 7),
    (6, 8),
    (7, 9),
    (8, 10),
    (9, 2),
    (10, 3),
    (12, 13),
    (13, 14),
    (14, 15),
    (15, 16),
    (16, 17),
    (17, 18),
    (18, 19),
    (19, 20),
    (20, 21),
    (21, 22),
    (22, 23),
    (23, 24),
    (24, 25),
    (25, 26),
    (26, 12),
    (27, 13),
    (28, 14),
    (29, 15),
    (30, 16);

-- Insert post tags
INSERT INTO tags (name)
VALUES
    ('Java'),
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
VALUES
    (1, 1), -- Java
    (2, 2), -- Spring Boot
    (3, 3), -- VSCode
    (4, 4), -- Microservices
    (5, 5), -- Docker
    (6, 6), -- REST
    (6, 7), -- GraphQL
    (7, 8), -- Kubernetes
    (8, 9), -- Python
    (9, 10); -- React

INSERT INTO level_info
VALUES
    (50, 5, 500, 1),
    (50, 4, 400, 2),
    (50, 3, 300, 3),
    (50, 2, 200, 4),
    (50, 1, 100, 5),
    (50, 5, 500, 6),
    (50, 5, 500, 7),
    (50, 5, 500, 8),
    (50, 5, 500, 9),
    (50, 5, 500, 10),
    (50, 5, 500, 11),
    (50, 5, 500, 12),
    (50, 4, 400, 13),
    (50, 3, 300, 14),
    (50, 2, 200, 15),
    (50, 1, 100, 16),
    (50, 5, 500, 17),
    (50, 5, 500, 18),
    (50, 5, 500, 19),
    (50, 5, 500, 20),
    (50, 5, 500, 21),
    (50, 4, 400, 22),
    (50, 3, 300, 23),
    (50, 2, 200, 24),
    (50, 1, 100, 25),
    (50, 5, 500, 26),
    (50, 5, 500, 27),
    (50, 5, 500, 28);

-- Reset foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
