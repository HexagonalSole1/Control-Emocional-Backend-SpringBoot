CREATE TABLE roles
(
    id         BIGINT NOT NULL,
    name       VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT NULL,
    role_id    BIGINT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users_roles
    ADD CONSTRAINT FK_USERS_ROLES_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT FK_USERS_ROLES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);