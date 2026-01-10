package com.library.bookservice.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.bookservice.infrastructure.persistence.entity.CategoryEntity;

/**
 * Spring Data JPA Repository for CategoryEntity
 * Spring generates implementation automatically
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Find category by name
     */
    Optional<CategoryEntity> findByName(String name);

    /**
     * Check if category exists by name
     */
    boolean existsByName(String name);

    /**
     * Count books in category
     */
    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.category.id = :categoryId")
    long countBooksByCategoryId(Long categoryId);
}