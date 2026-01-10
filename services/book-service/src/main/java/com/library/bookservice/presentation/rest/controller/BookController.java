package com.library.bookservice.presentation.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.bookservice.application.dto.BookSearchQuery;
import com.library.bookservice.application.port.input.BookService;
import com.library.bookservice.presentation.rest.dto.ApiResponse;
import com.library.bookservice.presentation.rest.dto.BookResponse;
import com.library.bookservice.presentation.rest.dto.CreateBookRequest;
import com.library.bookservice.presentation.rest.dto.ReserveBookRequest;
import com.library.bookservice.presentation.rest.dto.UpdateBookRequest;
import com.library.bookservice.presentation.rest.mapper.BookDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Book operations
 * Exposes HTTP endpoints
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private final BookService bookService;
    private final BookDtoMapper mapper;

    // ==================== CREATE ====================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book", description = "Creates a new book in the library catalog")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Book created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Book with ISBN already exists")
    })
    public ApiResponse<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        log.info("REST:  Creating book with ISBN: {}", request.getIsbn());

        // 1. Map Request → Command
        var command = mapper.toCommand(request);

        // 2. Call Application Service
        var appResponse = bookService.createBook(command);

        // 3. Map Application Response → Presentation Response
        var response = mapper.toDto(appResponse);

        // 4. Wrap in ApiResponse
        return ApiResponse.success(response, "Book created successfully");
    }

    // ==================== READ ====================

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID")
    public ApiResponse<BookResponse> getBookById(@PathVariable @Positive Long id) {
        log.info("REST: Getting book by ID: {}", id);

        var appResponse = bookService.getBookById(id);
        var response = mapper.toDto(appResponse);

        return ApiResponse.success(response, "Book retrieved successfully");
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books in the catalog")
    public ApiResponse<List<BookResponse>> getAllBooks() {
        log.info("REST:  Getting all books");

        var appResponses = bookService.getAllBooks();
        var responses = appResponses.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "Books retrieved successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by keyword in title, author, or description")
    public ApiResponse<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String keyword) {

        log.info("REST:  Searching books with keyword: {}", keyword);

        BookSearchQuery query = BookSearchQuery.builder()
                .keyword(keyword)
                .build();

        var appResponses = bookService.searchBooks(query);
        var responses = appResponses.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "Search completed successfully");
    }

    @GetMapping("/available")
    @Operation(summary = "Get available books", description = "Retrieves all books available for borrowing")
    public ApiResponse<List<BookResponse>> getAvailableBooks() {
        log.info("REST: Getting available books");

        var appResponses = bookService.getAvailableBooks();
        var responses = appResponses.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "Available books retrieved successfully");
    }

    @GetMapping("/category/{categoryName}")
    @Operation(summary = "Get books by category", description = "Retrieves all books in a specific category")
    public ApiResponse<List<BookResponse>> getBooksByCategory(
            @PathVariable String categoryName) {

        log.info("REST: Getting books by category: {}", categoryName);

        var appResponses = bookService.getBooksByCategory(categoryName);
        var responses = appResponses.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "Books in category retrieved successfully");
    }

    // ==================== UPDATE ====================

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates book information")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateBookRequest request) {

        log.info("REST: Updating book:  {}", id);

        var command = mapper.toCommand(request);
        var appResponse = bookService.updateBook(id, command);
        var response = mapper.toDto(appResponse);

        return ApiResponse.success(response, "Book updated successfully");
    }

    // ==================== DELETE ====================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book", description = "Deletes a book from the catalog")
    public void deleteBook(@PathVariable @Positive Long id) {
        log.info("REST: Deleting book:  {}", id);

        bookService.deleteBook(id);
    }

    // ==================== SPECIAL OPERATIONS ====================

    @PostMapping("/{id}/reserve")
    @Operation(summary = "Reserve a book", description = "Reserves a book for borrowing")
    public ApiResponse<Void> reserveBook(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ReserveBookRequest request) {

        log.info("REST: Reserving book {} for user {}", id, request.getUserId());

        var command = mapper.toCommand(id, request);
        bookService.reserveBook(command);

        return ApiResponse.success("Book reserved successfully");
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "Release book reservation", description = "Releases a book reservation (when returned)")
    public ApiResponse<Void> releaseBook(@PathVariable @Positive Long id) {
        log.info("REST: Releasing book:  {}", id);

        bookService.releaseBook(id);

        return ApiResponse.success("Book released successfully");
    }

    @PostMapping("/{id}/add-copies")
    @Operation(summary = "Add copies", description = "Adds more copies to book inventory")
    public ApiResponse<BookResponse> addCopies(
            @PathVariable @Positive Long id,
            @RequestParam @Min(1) int quantity) {

        log.info("REST: Adding {} copies to book:  {}", quantity, id);

        var appResponse = bookService.addCopies(id, quantity);
        var response = mapper.toDto(appResponse);

        return ApiResponse.success(response, quantity + " copies added successfully");
    }

    @PostMapping("/{id}/remove-copies")
    @Operation(summary = "Remove copies", description = "Removes copies from book inventory")
    public ApiResponse<BookResponse> removeCopies(
            @PathVariable @Positive Long id,
            @RequestParam @Min(1) int quantity) {

        log.info("REST: Removing {} copies from book: {}", quantity, id);

        var appResponse = bookService.removeCopies(id, quantity);
        var response = mapper.toDto(appResponse);

        return ApiResponse.success(response, quantity + " copies removed successfully");
    }
}