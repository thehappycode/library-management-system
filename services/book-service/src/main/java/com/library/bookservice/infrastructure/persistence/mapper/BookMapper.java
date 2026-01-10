package com.library.bookservice.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.library.bookservice.domain.model.Author;
import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.Category;
import com.library.bookservice.domain.model.ISBN;
import com.library.bookservice.domain.model.Inventory;
import com.library.bookservice.infrastructure.persistence.entity.BookEntity;

import lombok.RequiredArgsConstructor;

/**
 * Mapper between Book (Domain) and BookEntity (JPA)
 */
@Component
@RequiredArgsConstructor
public class BookMapper {

    private final CategoryMapper categoryMapper;

    /**
     * Convert Domain Model to JPA Entity
     */
    public BookEntity toEntity(Book book) {
        if (book == null) {
            return null;
        }

        return BookEntity.builder()
                .id(book.getId())
                .isbn(book.getIsbn().getValue())
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .description(book.getDescription())
                .category(categoryMapper.toEntity(book.getCategory()))
                .totalQuantity(book.getInventory().getTotalQuantity())
                .availableQuantity(book.getInventory().getAvailableQuantity())
                .borrowedQuantity(book.getInventory().getBorrowedQuantity())
                .status(book.getStatus())
                .coverImageUrl(book.getCoverImageUrl())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    /**
     * Convert JPA Entity to Domain Model
     */
    public Book toDomain(BookEntity entity) {
        if (entity == null) {
            return null;
        }

        // Reconstruct Value Objects
        ISBN isbn = ISBN.of(entity.getIsbn());
        Author author = Author.of(entity.getAuthorName());
        Category category = categoryMapper.toDomain(entity.getCategory());
        Inventory inventory = Inventory.of(
                entity.getTotalQuantity(),
                entity.getAvailableQuantity(),
                entity.getBorrowedQuantity());

        // Reconstruct Domain Model using factory method
        return Book.reconstruct(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                author,
                isbn,
                category,
                inventory,
                entity.getStatus(),
                entity.getCoverImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}