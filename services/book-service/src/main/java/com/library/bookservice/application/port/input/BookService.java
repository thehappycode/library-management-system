package com.library.bookservice.application.port.input;

import java.util.List;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.dto.BookSearchQuery;
import com.library.bookservice.application.dto.CreateBookCommand;
import com.library.bookservice.application.dto.ReserveBookCommand;
import com.library.bookservice.application.dto.UpdateBookCommand;

/**
 * Input Port - Defines what application can do
 * This is the PUBLIC API of the application layer
 */
public interface BookService {

    /**
     * Creates a new Book in the system
     * 
     * @param command
     * @return
     */
    BookResponse createBook(CreateBookCommand command);

    /**
     * Update book
     * 
     * @param id
     * @param command
     * @return
     */
    BookResponse updateBook(Long id, UpdateBookCommand command);

    /**
     * Delete a book
     * 
     * @param id
     */
    void deleteBook(Long id);

    /**
     * Get book by ID
     * 
     * @param id
     * @return
     */
    BookResponse getBookById(Long id);

    /**
     * Get all books
     * 
     * @return
     */
    List<BookResponse> getAllBooks();

    /**
     * Search books
     * 
     * @param query
     * @return
     */
    List<BookResponse> searchBooks(BookSearchQuery query);

    /**
     * Get available books
     * 
     * @return
     */
    List<BookResponse> getAvailableBooks();

    /**
     * Get books by category
     * 
     * @param categoryName
     * @return
     */
    List<BookResponse> getBooksByCategory(String categoryName);

    /**
     * Reserve a book
     * 
     * @param command
     */
    void reserveBook(ReserveBookCommand command);

    /**
     * Release book reservation
     * 
     * @param bookId
     */
    void releaseBook(Long bookId);

    /**
     * Add copies to book inventory
     * 
     * @param bookId
     * @param quantity
     * @return
     */
    BookResponse addCopies(Long bookId, int quantity);

    /**
     * Remove copies from book inventory
     * 
     * @param bookId
     * @param quantity
     * @return
     */
    BookResponse removeCopies(Long bookId, int quantity);
}
