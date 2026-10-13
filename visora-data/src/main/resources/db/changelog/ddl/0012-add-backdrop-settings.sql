-- liquibase formatted sql

-- changeset mac:1791200000000-1
ALTER TABLE backdrops
    ADD COLUMN settings jsonb;