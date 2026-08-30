-- Reference schema for the Identity module.
--
-- NOT applied at runtime: there is no Flyway yet, so Hibernate creates these tables
-- from the entity mappings in identity.domain.entities (ddl-auto). Keep this file in
-- step with those mappings - it is what a migration would be generated from.

CREATE SCHEMA IF NOT EXISTS identity;

-- 0. The principals themselves.
CREATE TABLE identity.users (
  user_id       BIGSERIAL PRIMARY KEY,
  email         VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  status        VARCHAR(20) NOT NULL,           -- ACTIVE, DISABLED, LOCKED
  company_id    UUID,                           -- owned by another context; referenced by id only
  branch_id     UUID
);

-- 1. The catalog of every possible grant (USER_CREATE, INVOICE_APPROVE, ...).
--    Add a new grant = insert a row here. No schema change ever.
CREATE TABLE identity.permissions (
  permission_id   BIGSERIAL PRIMARY KEY,
  permission_code VARCHAR(60) UNIQUE NOT NULL,   -- 'USER_CREATE', 'USER_DOWNLOAD'
  module          VARCHAR(40) NOT NULL,          -- 'USER', 'INVOICE', 'PRICE_MASTER'
  action          VARCHAR(20) NOT NULL,          -- CREATE, DELETE, APPROVE, DOWNLOAD, VIEW, EDIT, CANCEL
  description     VARCHAR(200),
  UNIQUE (module, action)
);

-- 2. Roles = named default templates.
CREATE TABLE identity.roles (
  role_id   BIGSERIAL PRIMARY KEY,
  role_code VARCHAR(40) UNIQUE NOT NULL,
  role_name VARCHAR(120) NOT NULL,
  is_system BOOLEAN NOT NULL DEFAULT FALSE
);

-- 3. The DEFAULT grants each role hands out (the template contents).
CREATE TABLE identity.role_permissions (
  role_id       BIGINT NOT NULL REFERENCES identity.roles(role_id) ON DELETE CASCADE,
  permission_id BIGINT NOT NULL REFERENCES identity.permissions(permission_id) ON DELETE CASCADE,
  PRIMARY KEY (role_id, permission_id)
);

-- 4. Which role(s) a user was created under — kept for reference / re-seeding.
CREATE TABLE identity.user_roles (
  user_id     BIGINT NOT NULL REFERENCES identity.users(user_id) ON DELETE CASCADE,
  role_id     BIGINT NOT NULL REFERENCES identity.roles(role_id),
  assigned_by BIGINT REFERENCES identity.users(user_id),
  assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, role_id)
);

-- 5. THE AUTHORITATIVE per-user grants. Every access check reads only this.
--    A row is an allow when is_granted, an explicit deny otherwise — "no row" and
--    "denied" are different states, which is what lets one permission be taken away
--    from a user without touching their role. source = MANUAL rows are never
--    overwritten by re-assigning a role.
CREATE TABLE identity.user_permissions (
  user_id       BIGINT NOT NULL REFERENCES identity.users(user_id) ON DELETE CASCADE,
  permission_id BIGINT NOT NULL REFERENCES identity.permissions(permission_id),
  is_granted    BOOLEAN NOT NULL DEFAULT TRUE,     -- TRUE = allow, FALSE = explicit deny
  source        VARCHAR(15) NOT NULL DEFAULT 'MANUAL'
                  CHECK (source IN ('ROLE_DEFAULT','MANUAL')),  -- provenance, for audit
  granted_by    BIGINT REFERENCES identity.users(user_id),
  granted_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, permission_id)
);
CREATE INDEX idx_user_perms_user ON identity.user_permissions(user_id);
