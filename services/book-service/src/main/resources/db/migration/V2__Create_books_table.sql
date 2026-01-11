-- Create books table
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    total_quantity INT NOT NULL DEFAULT 0,
    available_quantity INT NOT NULL DEFAULT 0,
    borrowed_quantity INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    cover_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    
    -- Indexes for better performance
    INDEX idx_isbn (isbn),
    INDEX idx_title (title),
    INDEX idx_author_name (author_name),
    INDEX idx_status (status),
    INDEX idx_category_id (category_id),
    
    -- Foreign key constraint
    CONSTRAINT fk_book_category 
        FOREIGN KEY (category_id) 
        REFERENCES categories(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- Check constraints
    CONSTRAINT chk_total_quantity CHECK (total_quantity >= 0),
    CONSTRAINT chk_available_quantity CHECK (available_quantity >= 0),
    CONSTRAINT chk_borrowed_quantity CHECK (borrowed_quantity >= 0),
    CONSTRAINT chk_status CHECK (status IN ('AVAILABLE', 'UNAVAILABLE', 'RESERVED', 'MAINTENANCE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
