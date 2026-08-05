-- ============================================================
-- Hibernate sequence
-- ============================================================
CREATE SCHEMA IF NOT EXISTS public;
CREATE SCHEMA IF NOT EXISTS school;
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;

-- ============================================================
-- roles
-- ============================================================
CREATE TABLE IF NOT EXISTS roles (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(20),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

INSERT INTO roles (name) SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');
INSERT INTO roles (name) SELECT 'ROLE_TEACHER'     WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_TEACHER');
INSERT INTO roles (name) SELECT 'ROLE_STUDENT'WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_STUDENT');

-- ============================================================
-- users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(255),
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    email           VARCHAR(255),
    verify_email    VARCHAR(255),
    phone_number    VARCHAR(255),
    password        VARCHAR(255),
    status          VARCHAR(255),
    profile         VARCHAR(255),
    change_password VARCHAR(255),
    created_at      TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255)
);

-- ============================================================
-- user_roles
-- ============================================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- ============================================================
-- refresh_tokens
-- ============================================================
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id          BIGSERIAL PRIMARY KEY,
    user_id     INT,
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ============================================================
-- post_categories
-- ============================================================
CREATE TABLE IF NOT EXISTS post_categories (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255),
    image_url  VARCHAR(255),
    status     VARCHAR(255),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- ============================================================
-- posts
-- ============================================================
CREATE TABLE IF NOT EXISTS posts (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    body        TEXT,
    total_view  INT     DEFAULT 0,
    likes       INT     NOT NULL DEFAULT 0,
    dislikes    INT     NOT NULL DEFAULT 0,
    status      VARCHAR(255),
    image       VARCHAR(255),
    category_id INT,
    user_id     INT,
    created_at  TIMESTAMP,
    created_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(255),
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES post_categories (id),
    CONSTRAINT fk_post_user     FOREIGN KEY (user_id)     REFERENCES users (id)
);

-- ============================================================
-- post_tags
-- ============================================================
CREATE TABLE IF NOT EXISTS post_tags (
    post_id INT          NOT NULL,
    tag     VARCHAR(100) NOT NULL,
    CONSTRAINT fk_post_tags_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

-- ============================================================
-- otp_logs
-- ============================================================
CREATE TABLE IF NOT EXISTS otp_logs (
    id          SERIAL PRIMARY KEY,
    token       VARCHAR(255),
    send_to     VARCHAR(255),
    otp         VARCHAR(255),
    otp_message VARCHAR(255),
    status      VARCHAR(255),
    action_type VARCHAR(255),
    created_at  TIMESTAMP,
    created_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(255)
);

-- ============================================================
-- image_details
-- ============================================================
CREATE TABLE IF NOT EXISTS image_details (
    id                 SERIAL PRIMARY KEY,
    file_path          VARCHAR(255),
    file_type          VARCHAR(255),
    file_name          VARCHAR(255),
    original_file_name VARCHAR(255),
    file_size          BIGINT,
    status             VARCHAR(255),
    created_at         TIMESTAMP,
    created_by         VARCHAR(255),
    updated_at         TIMESTAMP,
    updated_by         VARCHAR(255)
);
-- ============================================================
-- School schema
-- ============================================================
-- ============================================================
-- students
-- ============================================================
CREATE TABLE IF NOT EXISTS school.students (
    id              SERIAL PRIMARY KEY,
    user_id         INT UNIQUE,
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    gender          VARCHAR(20),
    date_of_birth   DATE,
    phone           VARCHAR(20),
    address         TEXT,
    photo           VARCHAR(255),
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),

    CONSTRAINT fk_student_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    );

-- ============================================================
-- teachers
-- ============================================================
CREATE TABLE IF NOT EXISTS school.teachers (
    id              SERIAL PRIMARY KEY,
    user_id         INT UNIQUE,
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    gender          VARCHAR(20),
    date_of_birth   DATE,
    phone           VARCHAR(20),
    address         TEXT,
    photo           VARCHAR(255),
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),

    CONSTRAINT fk_teacher_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    );




