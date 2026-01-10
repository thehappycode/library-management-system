package com.library.bookservice.application.port.output;

import java.util.List;
import java.util.Optional;

import com.library.bookservice.domain.model.Category;

/**
 * Output Port for Category Repository
 */
public interface CategoryRepositoryPort {

    /**
     * Saves a Category entity to the repository
     * 
     * @param category
     * @return
     */
    Category save(Category category);

    /**
     * Finds a Category entity by its ID
     * 
     * @param id
     * @return
     */
    Optional<Category> findById(Long id);

    /**
     * Finds a Category entity by its name
     * 
     * @param name
     * @return
     */
    Optional<Category> findByName(String name);

    /**
     * Finds all Category entities
     * 
     * @return
     */
    List<Category> findAll();

    /**
     * Deletes a Category entity from the repository
     * 
     * @param category
     */
    void delete(Category category);

    /**
     * Checks if a Category entity exists by its name
     * 
     * @param name
     * @return
     */
    boolean existsByName(String name);
}