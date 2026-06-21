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

insert into writers (firstname, lastname, status)
values ('firstname1', 'lastname1', 'ACTIVE'),
       ('firstname2', 'lastname2', 'DELETED'),
       ('firstname3', 'lastname3', 'UNDER_REVIEW'),
       ('firstname4', 'lastname4', 'ACTIVE'),
       ('firstname5', 'lastname5', 'DELETED'),
       ('firstname6', 'lastname6', 'UNDER_REVIEW'),
       ('firstname7', 'lastname7', 'DELETED'),
       ('firstname8', 'lastname8', 'ACTIVE');

insert into posts (title, content, created, updated, status, writer_id)
values ('post1', 'content1', now(), now(), 'ACTIVE', 1),
       ('post2', 'content2', now(), now(), 'DELETED', 2),
       ('post3', 'content3', now(), now(), 'UNDER_REVIEW', 3),
       ('post4', 'content4', now(), now(), 'DELETED', 3),
       ('post5', 'content5', now(), now(), 'ACTIVE', 5),
       ('post6', 'content6', now(), now(), 'UNDER_REVIEW', 6),
       ('post7', 'content7', now(), now(), 'ACTIVE', 7),
       ('post8', 'content8', now(), now(), 'DELETED', 7);

insert into post_label (post_id, label_id)
values (1, 1),(1, 2),
       (2, 3),(3, 5),
       (4, 5),(5, 6),
       (6, 7),(7, 8);