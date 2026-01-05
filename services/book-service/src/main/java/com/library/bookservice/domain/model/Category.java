package com.library.bookservice.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * Category Entity
 * Has identity (ID) but simple than Aggregate Root
 */

@Getter
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor to prevent direct instantiation
    private Category() {
    }

    /**
     * Factory method to create a new Category
     * 
     * @param name        Category name
     * @param description Category description
     * @return Category instance
     * @throws IllegalArgumentException if name is null or empty
     */
    public static Category create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");

        }

        Category category = new Category();
        category.name = name.trim();
        category.description = (description != null) ? description.trim() : null;
        category.createdAt = LocalDateTime.now();
        category.updatedAt = LocalDateTime.now();

        return category;

    }

    /**
     * Update category details
     * 
     * @param name        New category name
     * @param description New category description
     * @throws IllegalArgumentException if name is null or empty
     */
    public void update(String name, String description) {
        if (name != null && !name.isBlank()) {
            if (name.length() > 100) {
                throw new IllegalArgumentException("Category name cannot exceed 100 characters");
            }
            this.name = name.trim();
        }

        if (description != null) {
            this.description = description.trim();
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Check if the category can be deleted
     * 
     * @return true if deletable, false otherwise
     */
    public boolean canBeDeleted(int bookCount) {
        return bookCount == 0;
    }

    /**
     * Set the ID of the category (used by ORM)
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
}
