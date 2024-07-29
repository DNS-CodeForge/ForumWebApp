create database forum if not exis;
create or replace table forum.`level-info`

(
    id                int auto_increment
        primary key,
    current_exp       int not null,
    currnet_level     int not null,
    exp_to_next_level int not null
);

create or replace table forum.roles
(
    role_id   int auto_increment
        primary key,
    authority varchar(255) null
);

create or replace table forum.tags
(
    id   int auto_increment
        primary key,
    name varchar(32) not null
);

create or replace table forum.users
(
    id            int auto_increment
        primary key,
    first_name    varchar(32)                                                                                                                                                                                         null,
    last_name     varchar(32)                                                                                                                                                                                         null,
    email         varchar(255)                                                                                                                                                                                        not null,
    password      varchar(255)                                                                                                                                                                                        not null,
    username      varchar(20)                                                                                                                                                                                         not null,
    photo_url     varchar(255) default 'https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' null,
    level_info_id int                                                                                                                                                                                                 null,
    constraint UK_email
        unique (email),
    constraint UK_username
        unique (username),
    constraint UKmmvaefewbrvfvqqgohqiyj6qg
        unique (level_info_id),
    constraint FKaqpyevuifjirnjl2xvqwk0pon
        foreign key (level_info_id) references forum.`level-info` (id)
);

create or replace table forum.phone_numbers
(
    id      int auto_increment
        primary key,
    user_id int          null,
    number  varchar(255) null,
    constraint UK_user_id
        unique (user_id),
    constraint FK_phone_user
        foreign key (user_id) references forum.users (id)
);

create or replace table forum.posts
(
    id           int auto_increment
        primary key,
    user_id      int           null,
    created_date datetime(6)   null,
    description  varchar(8192) not null,
    title        varchar(64)   not null,
    constraint FK_post_user
        foreign key (user_id) references forum.users (id)
);

create or replace table forum.comments
(
    id      int auto_increment
        primary key,
    post_id int           null,
    content varchar(1024) not null,
    user_id int           null,
    constraint FK8omq0tc18jd43bu5tjh6jvraq
        foreign key (user_id) references forum.users (id),
    constraint FK_comment_post
        foreign key (post_id) references forum.posts (id)
);

create or replace table forum.likes
(
    id      int auto_increment
        primary key,
    post_id int null,
    user_id int null,
    constraint FK_like_post
        foreign key (post_id) references forum.posts (id),
    constraint FK_like_user
        foreign key (user_id) references forum.users (id)
);

create or replace table forum.post_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint FK_post_tags_post
        foreign key (post_id) references forum.posts (id),
    constraint FK_post_tags_tag
        foreign key (tag_id) references forum.tags (id)
);

create or replace table forum.user_role_junction
(
    role_id int not null,
    user_id int not null,
    primary key (role_id, user_id),
    constraint FK_user_role_junction_role
        foreign key (role_id) references forum.roles (role_id),
    constraint FK_user_role_junction_user
        foreign key (user_id) references forum.users (id)
);

