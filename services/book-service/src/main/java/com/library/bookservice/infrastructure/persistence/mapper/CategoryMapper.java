package com.library.bookservice.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.library.bookservice.domain.model.Category;
import com.library.bookservice.infrastructure.persistence.entity.CategoryEntity;

/**
 * Mapper between Category (Domain) and CategoryEntity (JPA)
 */
@Component
public class CategoryMapper {

    /**
     * Convert Domain Model to JPA Entity
     */
    public CategoryEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    /**
     * Convert JPA Entity to Domain Model
     */
    public Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return Category.reconstruct(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}