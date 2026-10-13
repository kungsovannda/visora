-- liquibase formatted sql

-- changeset mac:1789310000000-1
ALTER TABLE categories ADD display_order INT;

-- changeset mac:1789310000000-2
ALTER TABLE categories ADD CONSTRAINT uc_categories_name UNIQUE (name);