package com.library.bookservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.BookStatus;
import com.library.bookservice.infrastructure.persistence.entity.BookEntity;
import com.library.bookservice.infrastructure.persistence.mapper.BookMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter implementing BookRepositoryPort
 * Bridges Domain and Infrastructure layers
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final JpaBookRepository jpaRepository;
    private final BookMapper mapper;

    @Override
    public Book save(Book book) {
        log.debug("Saving book: {} (ID: {})", book.getTitle(), book.getId());

        BookEntity entity = mapper.toEntity(book);
        BookEntity savedEntity = jpaRepository.save(entity);

        Book savedBook = mapper.toDomain(savedEntity);
        log.debug("Book saved with ID: {}", savedBook.getId());

        return savedBook;
    }

    @Override
    public Optional<Book> findById(Long id) {
        log.debug("Finding book by ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        log.debug("Finding book by ISBN: {}", isbn);

        return jpaRepository.findByIsbn(isbn)
                .map(mapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        log.debug("Finding all books");

        List<BookEntity> entities = jpaRepository.findAll();
        log.debug("Found {} books", entities.size());

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByStatus(BookStatus status) {
        log.debug("Finding books by status: {}", status);

        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAvailableBooks() {
        log.debug("Finding available books");

        return jpaRepository.findAvailableBooks().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByKeyword(String keyword) {
        log.debug("Searching books by keyword: {}", keyword);

        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }

        return jpaRepository.searchByKeyword(keyword).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Book book) {
        log.debug("Deleting book: {} (ID: {})", book.getTitle(), book.getId());

        BookEntity entity = mapper.toEntity(book);
        jpaRepository.delete(entity);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return jpaRepository.existsByIsbn(isbn);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}