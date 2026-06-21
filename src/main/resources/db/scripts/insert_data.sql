CREATE DATABASE IF NOT EXISTS blog_db;
USE blog_db;

insert into labels (name, status)
values ('label1', 'ACTIVE'),
       ('label2', 'DELETED'),
       ('label3', 'UNDER_REVIEW'),
       ('label4', 'ACTIVE'),
       ('label5', 'DELETED'),
       ('label6', 'UNDER_REVIEW'),
       ('label7', 'DELETED'),
       ('label8', 'UNDER_REVIEW');

insert into posts (title, content, created, updated, status)
values ('post1', 'content1', now(), now(), 'ACTIVE'),
       ('post2', 'content2', now(), now(), 'DELETED'),
       ('post3', 'content3', now(), now(), 'UNDER_REVIEW'),
       ('post4', 'content4', now(), now(), 'DELETED'),
       ('post5', 'content5', now(), now(), 'ACTIVE'),
       ('post6', 'content6', now(), now(), 'UNDER_REVIEW'),
       ('post7', 'content7', now(), now(), 'ACTIVE'),
       ('post8', 'content8', now(), now(), 'DELETED');

insert into post_label (post_id, label_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (3, 5),
       (4, 5),
       (5, 6),
       (6, 7),
       (7, 8);