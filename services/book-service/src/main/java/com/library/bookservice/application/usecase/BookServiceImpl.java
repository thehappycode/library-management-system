package com.library.bookservice.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.dto.BookSearchQuery;
import com.library.bookservice.application.dto.CreateBookCommand;
import com.library.bookservice.application.dto.ReserveBookCommand;
import com.library.bookservice.application.dto.UpdateBookCommand;
import com.library.bookservice.application.port.input.BookService;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.domain.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final CreateBookUseCase createBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;
    private final GetBookByIdUseCase getBookByIdUseCase;
    private final GetAllBooksUseCase getAllBooksUseCase;
    private final ReserveBookUseCase reserveBookUseCase;
    private final BookRepositoryPort bookRepository;

    @Override
    public BookResponse createBook(CreateBookCommand command) {
        return createBookUseCase.execute(command);
    }

    @Override
    public BookResponse updateBook(Long id, UpdateBookCommand command) {
        return updateBookUseCase.execute(id, command);
    }

    @Override
    public void deleteBook(Long id) {
        deleteBookUseCase.execute(id);
    }

    @Override
    public BookResponse getBookById(Long id) {
        return getBookByIdUseCase.execute(id);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return getAllBooksUseCase.execute();
    }

    @Override
    public List<BookResponse> searchBooks(BookSearchQuery query) {
        log.info("Searching books with keyword: {}", query.getKeyword());

        List<Book> books = bookRepository.searchByKeyword(query.getKeyword());

        return books.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getAvailableBooks() {
        log.info("Getting available books");

        List<Book> books = bookRepository.findAvailableBooks();

        return books.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBooksByCategory(String categoryName) {
        log.info("Getting books by category: {}", categoryName);

        // For simplicity, using keyword search
        // In real implementation, should have dedicated method
        List<Book> books = bookRepository.searchByKeyword(categoryName);

        return books.stream()
                .filter(book -> book.getCategory().getName().equalsIgnoreCase(categoryName))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void reserveBook(ReserveBookCommand command) {
        reserveBookUseCase.execute(command);
    }

    @Override
    public void releaseBook(Long bookId) {
        log.info("Releasing book: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        book.releaseReservation();
        bookRepository.save(book);
    }

    @Override
    public BookResponse addCopies(Long bookId, int quantity) {
        log.info("Adding {} copies to book:  {}", quantity, bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        book.addCopies(quantity);
        Book updatedBook = bookRepository.save(book);

        return mapToResponse(updatedBook);
    }

    @Override
    public BookResponse removeCopies(Long bookId, int quantity) {
        log.info("Removing {} copies from book: {}", quantity, bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        book.removeCopies(quantity);
        Book updatedBook = bookRepository.save(book);

        return mapToResponse(updatedBook);
    }

    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn().getValue())
                .formattedIsbn(book.getFormattedIsbn())
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .authorFirstName(book.getAuthorFirstName())
                .authorLastName(book.getAuthorLastName())
                .description(book.getDescription())
                .categoryName(book.getCategory().getName())
                .totalQuantity(book.getInventory().getTotalQuantity())
                .availableQuantity(book.getInventory().getAvailableQuantity())
                .borrowedQuantity(book.getInventory().getBorrowedQuantity())
                .status(book.getStatus())
                .coverImageUrl(book.getCoverImageUrl())
                .availableForBorrowing(book.isAvailableForBorrowing())
                .popular(book.isPopular())
                .borrowRate(book.getInventory().getBorrowRate())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
