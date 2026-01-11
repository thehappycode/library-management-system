-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert some default categories
INSERT INTO categories (name, description) VALUES
('Fiction', 'Fictional literature including novels and short stories'),
('Non-Fiction', 'Factual books including biographies, history, and science'),
('Science', 'Scientific and technical books'),
('Technology', 'Technology and computer science books'),
('History', 'Historical books and references'),
('Biography', 'Biographical and autobiographical works'),
('Children', 'Books for children and young adults'),
('Reference', 'Reference materials including dictionaries and encyclopedias'),
('Education', 'Educational and textbooks'),
('Business', 'Business and management books');
