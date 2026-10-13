-- liquibase formatted sql

-- changeset mac:1787626374246-1
ALTER TABLE users
    ADD role_id BIGINT;

-- changeset mac:1787626374246-2
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

