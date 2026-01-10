package com.library.bookservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.bookservice.domain.model.BookStatus;
import com.library.bookservice.infrastructure.persistence.entity.BookEntity;

/**
 * Spring Data JPA Repository for BookEntity
 */
@Repository
public interface JpaBookRepository extends JpaRepository<BookEntity, Long> {

    /**
     * Find book by ISBN
     */
    Optional<BookEntity> findByIsbn(String isbn);

    /**
     * Check if book exists by ISBN
     */
    boolean existsByIsbn(String isbn);

    /**
     * Find books by status
     */
    List<BookEntity> findByStatus(BookStatus status);

    /**
     * Find available books (status = AVAILABLE and availableQuantity > 0)
     */
    @Query("SELECT b FROM BookEntity b WHERE b. status = 'AVAILABLE' AND b.availableQuantity > 0")
    List<BookEntity> findAvailableBooks();

    /**
     * Find books by author name
     */
    List<BookEntity> findByAuthorNameContainingIgnoreCase(String authorName);

    /**
     * Search books by keyword (title, author, or description)
     */
    @Query("SELECT b FROM BookEntity b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', : keyword, '%')) OR " +
            "LOWER(b.authorName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BookEntity> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Find books by category ID
     */
    @Query("SELECT b FROM BookEntity b WHERE b.category.id = : categoryId")
    List<BookEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Find books by category name
     */
    @Query("SELECT b FROM BookEntity b WHERE b.category.name = :categoryName")
    List<BookEntity> findByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Count available books
     */
    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.status = 'AVAILABLE' AND b.availableQuantity > 0")
    long countAvailableBooks();

    /**
     * Find books needing reorder (availableQuantity < threshold)
     */
    @Query("SELECT b FROM BookEntity b WHERE b.availableQuantity < : threshold AND b.status = 'AVAILABLE'")
    List<BookEntity> findBooksNeedingReorder(@Param("threshold") int threshold);

    /**
     * Find popular books (borrowRate > threshold)
     */
    @Query("SELECT b FROM BookEntity b WHERE " +
            "(CAST(b.borrowedQuantity AS double) / CAST(b.totalQuantity AS double)) > :borrowRateThreshold " +
            "AND b.totalQuantity > 0")
    List<BookEntity> findPopularBooks(@Param("borrowRateThreshold") double borrowRateThreshold);
}