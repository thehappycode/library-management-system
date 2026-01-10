package com.library.bookservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.library.bookservice.application.port.output.CategoryRepositoryPort;
import com.library.bookservice.domain.model.Category;
import com.library.bookservice.infrastructure.persistence.entity.CategoryEntity;
import com.library.bookservice.infrastructure.persistence.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter implementing CategoryRepositoryPort
 * Bridges Domain and Infrastructure layers
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final JpaCategoryRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        log.debug("Saving category: {}", category.getName());

        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(Long id) {
        log.debug("Finding category by ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        log.debug("Finding category by name: {}", name);

        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        log.debug("Finding all categories");

        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Category category) {
        log.debug("Deleting category: {}", category.getId());

        CategoryEntity entity = mapper.toEntity(category);
        jpaRepository.delete(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}