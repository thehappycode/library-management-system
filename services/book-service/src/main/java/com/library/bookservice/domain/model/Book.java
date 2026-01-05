package com.library.bookservice.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * Book Aggregate Root
 * Represents a book in the library system
 */

@Getter
public class Book {
    private Long id;
    private ISBN isbn;
    private String title;
    private Author author;
    private String description;
    private Category category;
    private Inventory inventory;
    private BookStatus status;
    private String coverImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor to prevent direct instantiation
    private Book() {
    }

    /**
     * Factory method to create a new Book
     * 
     * @param title    Book title
     * @param author   Book author
     * @param isbn     Book ISBN
     * @param category Book category
     * @return Book instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static Book create(
            String title,
            String description,
            Author author,
            ISBN isbn,
            Category category,
            Integer initialQuantity) {

        // Validate title
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        if (title.length() > 250) {
            throw new IllegalArgumentException("Book title cannot exceed 250 characters");
        }

        // Validate description
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Book description cannot be null or empty");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("Book description cannot exceed 1000 characters");
        }

        // Validate author
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        // Validate ISBN
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN cannot be null");
        }

        // Validate category
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        Book book = new Book();
        book.title = title.trim();
        book.description = description.trim();
        book.author = author;
        book.isbn = isbn;
        book.category = category;
        book.inventory = Inventory.initial(initialQuantity);
        book.status = BookStatus.AVAILABLE;
        book.createdAt = LocalDateTime.now();
        book.updatedAt = LocalDateTime.now();

        return book;
    }

    public static Book reconstruct(
            Long id,
            String title,
            String description,
            Author author,
            ISBN isbn,
            Category category,
            Inventory inventory,
            BookStatus status,
            String coverImageUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        Book book = new Book();
        book.id = id;
        book.title = title;
        book.description = description;
        book.author = author;
        book.isbn = isbn;
        book.category = category;
        book.inventory = inventory;
        book.status = status;
        book.coverImageUrl = coverImageUrl;
        book.createdAt = createdAt;
        book.updatedAt = updatedAt;
        return book;
    }

    /**
     * Update book information
     * 
     * @param title       New title
     * @param description New description
     * @param authorName  New author name
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public void updateInfo(
            String title,
            String description,
            String authorName) {

        if (title != null && !title.isBlank()) {
            if (title.length() > 250) {
                throw new IllegalArgumentException("Book title cannot exceed 250 characters");
            }
            this.title = title.trim();
        }

        if (description != null && !description.isBlank()) {
            if (description.length() > 1000) {
                throw new IllegalArgumentException("Book description cannot exceed 1000 characters");
            }
            this.description = description.trim();
        }

        if (authorName != null && !authorName.isBlank()) {
            this.author = Author.of(authorName);
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Change book category
     * 
     * @param newCategory New category
     * @throws IllegalArgumentException if newCategory is null
     */
    public void changeCategory(Category newCategory) {

        if (newCategory == null) {
            throw new IllegalArgumentException("New category cannot be null");
        }

        this.category = newCategory;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reserve a book copy
     * 
     * @throws IllegalStateException if the book is not available or no copies are
     *                               available
     */
    public void reserve() {

        if (this.status != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for reservation");
        }

        if (!this.inventory.hasAvailable()) {
            throw new IllegalStateException("No available copies to reserve");
        }

        this.inventory = this.inventory.decreaseAvailable(1);

        if (!this.inventory.hasAvailable()) {
            this.status = BookStatus.OUT_OF_STOCK;
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Release a reserved book copy (e.g., when returned)
     * 
     * @throws IllegalStateException if no copies are currently borrowed
     */
    public void releaseReservation() {

        if (this.inventory.getBorrowedQuantity() == 0) {
            throw new IllegalStateException("No borrowed books to release");
        }

        this.inventory = this.inventory.increaseAvailable(1);

        if (this.status == BookStatus.OUT_OF_STOCK) {
            this.status = BookStatus.AVAILABLE;
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Add more copies to the inventory
     * 
     * @param quantity Number of copies to add
     * @throws IllegalArgumentException if quantity is non-positive
     * @throws IllegalStateException    if the book is discontinued
     */
    public void addCopies(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Count to add must be positive");
        }

        if (this.status == BookStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot add copies to a discontinued book");
        }

        this.inventory = this.inventory.addCopies(quantity);

        if (this.status == BookStatus.OUT_OF_STOCK) {
            this.status = BookStatus.AVAILABLE;
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Remove copies from the inventory
     * 
     * @param quantity Number of copies to remove
     * @throws IllegalArgumentException if quantity is non-positive
     * @throws IllegalStateException    if removing copies would lead to negative
     *                                  total quantity
     */
    public void removeCopies(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Count to remove must be positive");
        }

        this.inventory = this.inventory.removeCopies(quantity);
        if (this.status == BookStatus.AVAILABLE && !this.inventory.hasAvailable()) {
            this.status = BookStatus.OUT_OF_STOCK;
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Discontinue the book
     * 
     * @throws IllegalStateException if there are borrowed copies
     */
    public void discontinue() {
        if (this.inventory.getBorrowedQuantity() > 0) {
            throw new IllegalStateException("Cannot discontinue a book with borrowed copies");
        }
        this.status = BookStatus.DISCONTINUED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reactivate a discontinued book
     * 
     * @throws IllegalStateException if the book is not discontinued
     */
    public void reactivate() {
        if (this.status != BookStatus.DISCONTINUED) {
            throw new IllegalStateException("Only discontinued books can be reactivated");
        }
        this.status = this.inventory.hasAvailable()
                ? BookStatus.AVAILABLE
                : BookStatus.OUT_OF_STOCK;

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Upload or update the cover image URL
     * 
     * @param coverImageUrl URL of the cover image
     * @throws IllegalArgumentException if coverImageUrl is null or empty
     */
    public void uploadCoverImage(String coverImageUrl) {
        if (coverImageUrl == null || coverImageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        this.coverImageUrl = coverImageUrl.trim();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Remove the cover image URL
     * 
     * @throws IllegalStateException if there is no cover image to remove
     */
    public void removeCoverImage() {
        this.coverImageUrl = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Check if book is available for borrowing
     * 
     * @return true if available, false otherwise
     */
    public boolean isAvailableForBorrowing() {
        return this.status == BookStatus.AVAILABLE && this.inventory.hasAvailable();
    }

    /**
     * Check if book is popular (more than 80% borrowed)
     * 
     * @return true if popular, false otherwise
     */
    public boolean isPopular() {
        return this.inventory.getBorrowRate() > 0.8;
    }

    /**
     * Check if book needs reordering
     * 
     * @param threshold Reorder threshold
     */
    public boolean needsReorder(int threshold) {
        return this.inventory.needsReorder(threshold);
    }

    /**
     * Check if book can be deleted
     * 
     * @return true if deletable, false otherwise
     */
    public boolean canBeDeleted() {
        return this.inventory.getBorrowedQuantity() == 0;
    }

    /**
     * Get formatted ISBN for display
     * 
     * @return Formatted ISBN string
     */
    public String getFormattedIsbn() {
        return this.isbn.formatted();
    }

    /**
     * Get author's first name
     * 
     * @return Author's first name
     */
    public String getAuthorFirstName() {
        return this.author.getFirstName();
    }

    /**
     * Get author's last name
     * 
     * @return Author's last name
     */
    public String getAuthorLastName() {
        return this.author.getLastName();
    }

    /**
     * Set the ID of the category (used by ORM)
     * 
     * @param id ID of the book
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", status=" + status +
                ", available=" + inventory.getAvailableQuantity() +
                '}';
    }
}
