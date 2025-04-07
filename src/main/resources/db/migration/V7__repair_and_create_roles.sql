-- Comprobamos si la tabla roles ya tiene auto_increment
SET @query = CONCAT('SELECT COUNT(*) INTO @exists FROM information_schema.columns ',
                   'WHERE table_schema = DATABASE() ',
                   'AND table_name = "roles" ',
                   'AND column_name = "id" ',
                   'AND extra LIKE "%auto_increment%"');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Si no tiene auto_increment, lo añadimos de forma segura
SET @alter_roles = IF(@exists = 0,
    'ALTER TABLE roles MODIFY id BIGINT AUTO_INCREMENT NOT NULL',
    'SELECT "Roles already has auto_increment, no action needed" AS message');
PREPARE stmt FROM @alter_roles;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Insertar roles básicos si no existen
INSERT INTO roles (name, created_at, updated_at)
SELECT 'USER', NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

INSERT INTO roles (name, created_at, updated_at)
SELECT 'ADMIN', NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');