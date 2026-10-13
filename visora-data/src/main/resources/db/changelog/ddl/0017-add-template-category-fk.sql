-- liquibase formatted sql

-- changeset mac:1791300000002-1
ALTER TABLE templates
    ADD COLUMN category_id BIGINT;

-- changeset mac:1791300000002-2
UPDATE templates t
SET category_id = c.id
FROM categories c
WHERE t.category_uuid IS NOT NULL
  AND t.category_uuid <> ''
  AND c.uuid = t.category_uuid;

-- changeset mac:1791300000002-3
ALTER TABLE templates
    ADD CONSTRAINT FK_TEMPLATES_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

-- changeset mac:1791300000002-4
CREATE INDEX idx_templates_category ON templates (category_id);

-- changeset mac:1791300000002-5
ALTER TABLE templates
    DROP COLUMN category_uuid;
