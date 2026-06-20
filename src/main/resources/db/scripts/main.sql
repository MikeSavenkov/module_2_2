CREATE DATABASE IF NOT EXISTS blog_db;
USE blog_db;

insert into labels (name, status)
values ('label1', 'active'),
       ('label2', 'DELETED'),
       ('label3', 'UNDER_REVIEW'),
       ('label4', 'ACTIVE'),
       ('label5', 'DELETED'),
       ('label6', 'UNDER_REVIEW'),
       ('label7', 'DELETED'),
       ('label8', 'UNDER_REVIEW');
