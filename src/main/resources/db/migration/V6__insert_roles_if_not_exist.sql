-- src/main/resources/db/migration/V6__insert_roles_if_not_exist.sql

-- Insertar rol USER solo si no existe
INSERT INTO roles (name, created_at, updated_at)
SELECT 'USER', NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

-- Insertar rol ADMIN solo si no existe
INSERT INTO roles (name, created_at, updated_at)
SELECT 'ADMIN', NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');