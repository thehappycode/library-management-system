-- Insert sample books for testing
-- Note: Make sure category_id matches your actual category IDs

-- Fiction books
INSERT INTO books (isbn, title, author_name, description, category_id, total_quantity, available_quantity, borrowed_quantity, status) VALUES
('978-0-061-96436-7', 'To Kill a Mockingbird', 'Harper Lee', 'A gripping, heart-wrenching, and wholly remarkable tale of coming-of-age in a South poisoned by virulent prejudice.', 1, 5, 5, 0, 'AVAILABLE'),
('978-0-451-52493-5', '1984', 'George Orwell', 'A dystopian social science fiction novel and cautionary tale about the future.', 1, 3, 2, 1, 'AVAILABLE'),
('978-0-7432-7356-5', 'The Great Gatsby', 'F. Scott Fitzgerald', 'The story of the mysteriously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.', 1, 4, 4, 0, 'AVAILABLE');

-- Science books
INSERT INTO books (isbn, title, author_name, description, category_id, total_quantity, available_quantity, borrowed_quantity, status) VALUES
('978-0-385-50986-1', 'A Brief History of Time', 'Stephen Hawking', 'A landmark volume in science writing by one of the great minds of our time.', 3, 3, 2, 1, 'AVAILABLE'),
('978-0-553-38016-3', 'The Grand Design', 'Stephen Hawking', 'Explores the fundamental questions about the universe and our existence.', 3, 2, 2, 0, 'AVAILABLE');

-- Technology books
INSERT INTO books (isbn, title, author_name, description, category_id, total_quantity, available_quantity, borrowed_quantity, status) VALUES
('978-0-132-35088-4', 'Clean Code', 'Robert C. Martin', 'A Handbook of Agile Software Craftsmanship', 4, 5, 3, 2, 'AVAILABLE'),
('978-0-201-63361-0', 'Design Patterns', 'Gang of Four', 'Elements of Reusable Object-Oriented Software', 4, 3, 3, 0, 'AVAILABLE'),
('978-0-596-00781-5', 'Head First Java', 'Kathy Sierra', 'A Brain-Friendly Guide to Java Programming', 4, 4, 4, 0, 'AVAILABLE');

-- Business books
INSERT INTO books (isbn, title, author_name, description, category_id, total_quantity, available_quantity, borrowed_quantity, status) VALUES
('978-0-066-62099-2', 'Good to Great', 'Jim Collins', 'Why Some Companies Make the Leap and Others Don\'t', 10, 2, 2, 0, 'AVAILABLE'),
('978-1-591-84805-6', 'The Lean Startup', 'Eric Ries', 'How Today\'s Entrepreneurs Use Continuous Innovation to Create Radically Successful Businesses', 10, 3, 1, 2, 'AVAILABLE');
