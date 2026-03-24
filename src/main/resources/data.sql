-- Insert categories
INSERT INTO categories (id, name, description) VALUES (1, 'Fiction', 'Fictional literature');
INSERT INTO categories (id, name, description) VALUES (2, 'Non-Fiction', 'Non-fictional works');
INSERT INTO categories (id, name, description) VALUES (3, 'Science', 'Scientific books');
INSERT INTO categories (id, name, description) VALUES (4, 'Technology', 'Technology and programming books');
INSERT INTO categories (id, name, description) VALUES (5, 'History', 'Historical books');

-- Insert authors
INSERT INTO authors (id, first_name, last_name, biography) VALUES (1, 'George', 'Orwell', 'English novelist and essayist');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (2, 'J.K.', 'Rowling', 'British author of Harry Potter');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (3, 'Robert', 'Martin', 'Software engineer and author');

-- Insert users (password: password123 - BCrypt encoded)
INSERT INTO users (id, username, email, password, first_name, last_name, role, enabled) VALUES
(1, 'admin', 'admin@bibliotech.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', 'ROLE_ADMIN', true);
INSERT INTO users (id, username, email, password, first_name, last_name, role, enabled) VALUES
(2, 'librarian', 'librarian@bibliotech.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lib', 'Rarian', 'ROLE_LIBRARIAN', true);
INSERT INTO users (id, username, email, password, first_name, last_name, role, enabled) VALUES
(3, 'user1', 'user1@bibliotech.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', 'ROLE_USER', true);

-- Insert books
INSERT INTO books (id, title, isbn, author_id, category_id, total_copies, available_copies, description, publication_year) VALUES
(1, '1984', '978-0-452-28423-4', 1, 1, 5, 5, 'Dystopian novel', 1949);
INSERT INTO books (id, title, isbn, author_id, category_id, total_copies, available_copies, description, publication_year) VALUES
(2, 'Animal Farm', '978-0-452-28424-1', 1, 1, 3, 3, 'Political allegory', 1945);
INSERT INTO books (id, title, isbn, author_id, category_id, total_copies, available_copies, description, publication_year) VALUES
(3, 'Clean Code', '978-0-13-235088-4', 3, 4, 4, 4, 'A handbook of agile software craftsmanship', 2008);
