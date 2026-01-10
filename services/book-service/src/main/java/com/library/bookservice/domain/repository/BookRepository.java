package com.library.bookservice.domain.repository;

import java.util.List;
import java.util.Optional;

import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.BookStatus;

/**
 * Repository interface for managing Book entities
 */
public interface BookRepository {

    /**
     * Saves a Book entity to the repository
     * 
     * @param book
     * @return
     */
    Book save(Book book);

    /**
     * Finds a Book entity by its ID
     * 
     * @param id
     * @return
     */
    Optional<Book> findById(Long id);

    /**
     * Finds a Book entity by its ISBN
     * 
     * @param isbn
     * @return
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Retrieves all Book entities from the repository
     * 
     * @return
     */
    List<Book> findAll();

    /**
     * Finds books by category
     * 
     * @param category
     * @return
     */
    List<Book> findByCategory(String category);

    /**
     * Finds books by their status
     * 
     * @param status
     * @return
     */
    List<Book> findByStatus(BookStatus status);

    /**
     * Finds all available books
     * 
     * @return
     */
    List<Book> findAvailableBooks();

    /**
     * Finds books by author name
     * 
     * @param authorName
     * @return
     */
    List<Book> findByAuthorName(String authorName);

    /**
     * Searches books by a keyword in title or description
     * 
     * @param keyword
     * @return
     */
    List<Book> searchByKeyword(String keyword);

    /**
     * Deletes a Book entity from the repository
     * 
     * @param book
     */
    void delete(Book book);

    /**
     * Checks if a Book exists by its ISBN
     * 
     * @param isbn
     * @return
     */
    boolean existsByIsbn(String isbn);

    /**
     * Counts the number of available books
     * 
     * @return
     */
    long countAvailableBooks();

    /**
     * Finds books that need to be reordered based on a threshold
     * 
     * @param threshold
     * @return
     */
    List<Book> findBooksNeedingReorder(int threshold);

    /**
     * Finds popular books based on borrow rate
     * (borrow rate > threshold)
     * 
     * @param borrowRateThreshold
     * @return
     */

    List<Book> findPopularBooks(int borrowRateThreshold);
}
