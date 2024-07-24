USE forum;

-- Insert mock data into roles
INSERT INTO roles (authority) VALUES ('USER'), ('MODERATOR'), ('ADMIN');

-- Insert mock data into tags
INSERT INTO tags (name) VALUES ('Java'), ('Spring'), ('Hibernate'), ('JPA'), ('SQL'),
                               ('Docker'), ('Kubernetes'), ('Microservices'), ('DevOps'),
                               ('Python'), ('JavaScript'), ('TypeScript'), ('React'),
                               ('Angular'), ('Vue'), ('HTML'), ('CSS'), ('NoSQL'),
                               ('Machine Learning'), ('AI');

-- Insert mock data into users
INSERT INTO users (first_name, last_name, email, password, username)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'password123', 'johndoe'),
    ('Brad', 'Doe', 'brad.doe@example.com', 'password123', 'Braddoe'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'password123', 'janesmith'),
    ('Alice', 'Johnson', 'alice.johnson@example.com', 'password123', 'alicejohnson'),
    ('Bob', 'Brown', 'bob.brown@example.com', 'password123', 'bobbrown'),
    ('Charlie', 'Davis', 'charlie.davis@example.com', 'password123', 'charliedavis'),
    ('David', 'Wilson', 'david.wilson@example.com', 'password123', 'davidwilson'),
    ('Eva', 'Moore', 'eva.moore@example.com', 'password123', 'evamoore'),
    ('Frank', 'Taylor', 'frank.taylor@example.com', 'password123', 'franktaylor'),
    ('Grace', 'Anderson', 'grace.anderson@example.com', 'password123', 'graceanderson'),
    ('Hank', 'Thomas', 'hank.thomas@example.com', 'password123', 'hankthomas'),
    ('Ivy', 'Jackson', 'ivy.jackson@example.com', 'password123', 'ivyjackson'),
    ('Jack', 'White', 'jack.white@example.com', 'password123', 'jackwhite'),
    ('Kathy', 'Harris', 'kathy.harris@example.com', 'password123', 'kathyharris'),
    ('Leo', 'Martin', 'leo.martin@example.com', 'password123', 'leomartin'),
    ('Mia', 'Clark', 'mia.clark@example.com', 'password123', 'miaclark'),
    ('Nina', 'Lewis', 'nina.lewis@example.com', 'password123', 'ninalewis'),
    ('Oscar', 'Walker', 'oscar.walker@example.com', 'password123', 'oscarwalker'),
    ('Paul', 'Hall', 'paul.hall@example.com', 'password123', 'paulhall'),
    ('Quinn', 'Allen', 'quinn.allen@example.com', 'password123', 'quinnallen');

-- Assign roles to users
INSERT INTO user_role_junction (role_id, user_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
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
INSERT INTO comments (post_id, content)
VALUES
    (1, 'Great overview of Java 16!'), (1, 'Very informative.'), (2, 'Spring Boot is awesome!'),
    (3, 'Helpful article on Hibernate.'), (4, 'Good insights on JPA.'), (5, 'Nice comparison!'),
    (6, 'Docker is a game changer.'), (7, 'Kubernetes is powerful!'), (8, 'Scalability is key.'),
    (9, 'DevOps practices are essential.'), (10, 'Python is great for data analysis.'),
    (11, 'JavaScript frameworks are evolving fast.'), (12, 'TypeScript improves productivity.'),
    (13, 'React is my favorite UI library.'), (14, 'Angular is robust for large apps.'),
    (15, 'Vue.js is very flexible.'), (16, 'HTML and CSS are foundational.'),
    (17, 'NoSQL databases offer great performance.'), (18, 'Machine Learning is fascinating.'),
    (19, 'AI will shape the future.'), (20, 'SQL queries can be very powerful.');

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
