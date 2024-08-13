create or replace table SPRING_SESSION
(
    PRIMARY_ID            char(36)     not null
        primary key,
    SESSION_ID            char(36)     not null,
    CREATION_TIME         bigint       not null,
    LAST_ACCESS_TIME      bigint       not null,
    MAX_INACTIVE_INTERVAL int          not null,
    EXPIRY_TIME           bigint       not null,
    PRINCIPAL_NAME        varchar(100) null,
    constraint SPRING_SESSION_IX1
        unique (SESSION_ID)
)
    row_format = DYNAMIC;

create or replace index SPRING_SESSION_IX2
    on SPRING_SESSION (EXPIRY_TIME);

create or replace index SPRING_SESSION_IX3
    on SPRING_SESSION (PRINCIPAL_NAME);

create or replace table SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID char(36)     not null,
    ATTRIBUTE_NAME     varchar(200) not null,
    ATTRIBUTE_BYTES    blob         not null,
    primary key (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    constraint SPRING_SESSION_ATTRIBUTES_FK
        foreign key (SESSION_PRIMARY_ID) references SPRING_SESSION (PRIMARY_ID)
            on delete cascade
)
    row_format = DYNAMIC;

create or replace index SPRING_SESSION_ATTRIBUTES_IX1
    on SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);

create or replace table level_info
(
    current_exp       int not null,
    current_level     int not null,
    exp_to_next_level int not null,
    user_id           int not null
        primary key
);

create or replace table roles
(
    role_id   int auto_increment
        primary key,
    authority varchar(255) null
);

create or replace table tags
(
    id   int auto_increment
        primary key,
    name varchar(32) not null
);

create or replace table users
(
    id         int auto_increment
        primary key,
    username   varchar(20)  not null,
    first_name varchar(32)  null,
    last_name  varchar(32)  null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    photo_url  varchar(255) not null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UKr43af9ap4edm43mmtq01oddj6
        unique (username)
);

create or replace table phone_numbers
(
    user_id int          not null
        primary key,
    number  varchar(255) null,
    constraint FKg077extnnxwv904qjw2kwinpg
        foreign key (user_id) references users (id)
);

create or replace table posts
(
    id           int auto_increment
        primary key,
    user_id      int           null,
    created_date datetime(6)   null,
    title        varchar(64)   not null,
    description  varchar(8192) not null,
    constraint FK5lidm6cqbc7u4xhqpxm898qme
        foreign key (user_id) references users (id)
);

create or replace table comments
(
    id      int auto_increment
        primary key,
    post_id int           not null,
    user_id int           null,
    content varchar(1024) not null,
    constraint FK8omq0tc18jd43bu5tjh6jvraq
        foreign key (user_id) references users (id),
    constraint FKh4c7lvsc298whoyd4w9ta25cr
        foreign key (post_id) references posts (id)
);

create or replace table likes
(
    id      int auto_increment
        primary key,
    post_id int null,
    user_id int null,
    constraint FKnvx9seeqqyy71bij291pwiwrg
        foreign key (user_id) references users (id),
    constraint FKry8tnr4x2vwemv2bb0h5hyl0x
        foreign key (post_id) references posts (id)
);

create or replace table post_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint FKkifam22p4s1nm3bkmp1igcn5w
        foreign key (post_id) references posts (id),
    constraint FKm6cfovkyqvu5rlm6ahdx3eavj
        foreign key (tag_id) references tags (id)
);

create or replace table user_role_junction
(
    role_id int not null,
    user_id int not null,
    primary key (role_id, user_id),
    constraint FK5aqfsa7i8mxrr51gtbpcvp0v1
        foreign key (user_id) references users (id),
    constraint FKhybpcwvq8snjhbxawo25hxous
        foreign key (role_id) references roles (role_id)
);
SET FOREIGN_KEY_CHECKS = 0;
INSERT forum.roles VALUE (1, 'USER'), (2, 'MODERATOR'), (3, 'ADMIN'), (4, 'BANNED');
INSERT forum.users VALUES (1, 'admin','admin','admin','admin@admin.com','$2a$10$4FyOuu59z.W4TFCxyPU2Se9i.cnyrU1eHroCpGJPJXUra8re64Oly', 'NoPhoto');


INSERT forum.user_role_junction VALUE (1, 1), (1, 3);
INSERT forum.level_info VALUE (50,5, 500, 1);
SET FOREIGN_KEY_CHECKS = 1;