-- liquibase formatted sql

-- changeset mac:1787739805444-1
ALTER TABLE roles
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE roles
    ADD created_by VARCHAR(255);
ALTER TABLE roles
    ADD is_deleted BOOLEAN;
ALTER TABLE roles
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE roles
    ADD updated_by VARCHAR(255);

-- changeset mac:1787739805444-2
ALTER TABLE users
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE users
    ADD created_by VARCHAR(255);
ALTER TABLE users
    ADD is_deleted BOOLEAN;
ALTER TABLE users
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE users
    ADD updated_by VARCHAR(255);

-- changeset mac:1787739805444-6
ALTER TABLE roles
    ALTER COLUMN is_deleted SET NOT NULL;

-- changeset mac:1787739805444-8
ALTER TABLE users
    ALTER COLUMN is_deleted SET NOT NULL;

