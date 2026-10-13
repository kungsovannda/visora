-- liquibase formatted sql

-- changeset mac:1789286582888-1
ALTER TABLE storages ADD COLUMN IF NOT EXISTS uuid VARCHAR(255);

-- changeset mac:1789286600000-1
UPDATE storages SET uuid = md5(random()::text || clock_timestamp()::text) WHERE uuid IS NULL;

-- changeset mac:1789286600000-2
ALTER TABLE storages ALTER COLUMN uuid SET NOT NULL;

-- changeset mac:1789286600000-3
CREATE UNIQUE INDEX IF NOT EXISTS uc_storages_uuid ON storages (uuid);