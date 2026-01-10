package com.library.bookservice.application.port.output;

import java.util.List;
import java.util.Optional;

import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.BookStatus;

/**
 * Output Port for Book Repository
 * Infrastructure layer will implement this
 */

public interface BookRepositoryPort {

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
     * Finds all Book entities
     * 
     * @return
     */
    List<Book> findAll();

    /**
     * Finds Book entities by their status
     * 
     * @param status
     * @return
     */
    List<Book> findByStatus(BookStatus status);

    /**
     * Finds all available Book entities
     * 
     * @return
     */
    List<Book> findAvailableBooks();

    /**
     * Searches Book entities by a keyword
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
     * Checks if a Book entity exists by its ISBN
     * 
     * @param isbn
     * @return
     */
    boolean existsByIsbn(String isbn);

    /**
     * Counts the total number of Book entities
     * 
     * @return
     */
    long count();
}