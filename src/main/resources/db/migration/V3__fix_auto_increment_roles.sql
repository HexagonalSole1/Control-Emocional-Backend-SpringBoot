-- 🔹 1. Eliminar la clave foránea en users_roles que referencia a roles
ALTER TABLE users_roles DROP FOREIGN KEY FK_USERS_ROLES_ON_ROLE;

-- 🔹 2. Modificar la columna id en roles para agregar AUTO_INCREMENT
ALTER TABLE roles MODIFY id BIGINT AUTO_INCREMENT NOT NULL;

-- 🔹 3. Restaurar la clave foránea en users_roles
ALTER TABLE users_roles ADD CONSTRAINT FK_USERS_ROLES_ON_ROLE
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;
