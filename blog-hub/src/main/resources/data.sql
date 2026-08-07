-- Blog Hub sample data
-- 2 users + 1 admin + 3 posts
-- Password for all accounts: password123

USE `blog-hub`;

-- Make sure the roles exist
INSERT INTO roles (name)
SELECT 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

INSERT INTO roles (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Sample users
-- BCrypt hash below is for: password123
INSERT INTO users
    (name, email, password, bio, profile_image, created_at, updated_at, role_id)
VALUES
    (
        'Alex Johnson',
        'alex@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Backend developer and technology enthusiast.',
        'alex.png',
        NOW(),
        NULL,
        (SELECT id FROM roles WHERE name = 'USER' LIMIT 1)
    ),
    (
        'Emma Wilson',
        'emma@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Software developer who enjoys writing about programming.',
        'emma.png',
        NOW(),
        NULL,
        (SELECT id FROM roles WHERE name = 'USER' LIMIT 1)
    ),
    (
        'System Admin',
        'admin@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Blog Hub administrator.',
        'admin.png',
        NOW(),
        NULL,
        (SELECT id FROM roles WHERE name = 'ADMIN' LIMIT 1)
    );

-- Three posts belonging to the two normal users
INSERT INTO posts
    (title, content, status, user_id, created_at, updated_at)
VALUES
    (
        'Getting Started with Spring Boot',
        'Spring Boot makes it easier to build production-ready Java applications. This post introduces the basic project structure, controllers, services, and repositories.',
        'PUBLISHED',
        (SELECT id FROM users WHERE email = 'alex@example.com' LIMIT 1),
        NOW(),
        NULL
    ),
    (
        'Understanding REST APIs',
        'REST APIs provide a simple way for applications to communicate over HTTP. This post covers common HTTP methods, resources, status codes, and API design basics.',
        'PUBLISHED',
        (SELECT id FROM users WHERE email = 'alex@example.com' LIMIT 1),
        NOW(),
        NULL
    ),
    (
        'Learning Database Design',
        'A well-designed database helps applications remain reliable and maintainable. This post discusses tables, relationships, primary keys, and foreign keys.',
        'DRAFT',
        (SELECT id FROM users WHERE email = 'emma@example.com' LIMIT 1),
        NOW(),
        NULL
    );