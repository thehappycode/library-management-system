package com.library.bookservice.domain.model;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Value;

/**
 * Value Object representing Inventory
 * Immutable and encapsulates inventory-related data
 */

@Value
public class Inventory {
    Integer totalQuantity;
    Integer availableQuantity;
    Integer borrowedQuantity;

    private Inventory(
            Integer totalQuantity,
            Integer availableQuantity,
            Integer borrowedQuantity) {

        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.borrowedQuantity = borrowedQuantity;
    }

    /**
     * Factory method to create initial an Inventory
     * 
     * @param totalQuantity     Total number of copies
     * @param availableQuantity Number of available copies
     * @param borrowedQuantity  Number of borrowed copies
     * @return Inventory instance
     * @throws IllegalArgumentException if any quantity is negative or inconsistent
     */
    public static Inventory initial(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be null or negative");
        }
        return new Inventory(quantity, quantity, 0);
    }

    /**
     * Reconstruct inventory (e.g., from database)
     * 
     * @param totalQuantity     Total number of copies
     * @param availableQuantity Number of available copies
     * @param borrowedQuantity  Number of borrowed copies
     * @return Inventory instance
     * @throws IllegalArgumentException if any quantity is negative or inconsistent
     */
    public static Inventory of(
            Integer totalQuantity,
            Integer availableQuantity,
            Integer borrowedQuantity) {

        if (totalQuantity < 0
                || availableQuantity < 0
                || borrowedQuantity < 0) {
            throw new IllegalArgumentException("Inventory quantities cannot be negative");
        }

        if (availableQuantity + borrowedQuantity != totalQuantity) {
            throw new IllegalArgumentException(
                    "Total quantity must equal available plus borrowed quantities. " +
                            "Total: " + totalQuantity +
                            ", Available: " + availableQuantity +
                            ", Borrowed: " + borrowedQuantity);
        }

        return new Inventory(
                totalQuantity,
                availableQuantity,
                borrowedQuantity);
    }

    /**
     * Decrease available quantity when books are borrowed
     * 
     * @param count Number of books to borrow
     * @return New Inventory instance with updated quantities
     * @throws IllegalArgumentException if count is negative or insufficient
     *                                  available books
     */
    public Inventory decreaseAvailable(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count to decrease cannot be negative");
        }

        var newAvailable = this.availableQuantity - count;
        if (newAvailable < 0) {
            throw new IllegalArgumentException("Insufficient available books."
                    + "Avaiable: " + this.availableQuantity);
        }

        return new Inventory(
                totalQuantity,
                newAvailable,
                borrowedQuantity + count);
    }

    /**
     * Increase available quantity when books are returned
     * 
     * @param count Number of books to return
     * @return New Inventory instance with updated quantities
     * @throws IllegalArgumentException if count is negative or exceeds total
     *                                  quantity
     */
    public Inventory increaseAvailable(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count to increase cannot be negative");
        }

        var newAvailable = this.availableQuantity + count;
        if (newAvailable > totalQuantity) {
            throw new IllegalArgumentException("Available books cannot exceed total quantity."
                    + "Total: " + this.totalQuantity);
        }

        return new Inventory(
                totalQuantity,
                newAvailable,
                borrowedQuantity - count);
    }

    /**
     * Add new copies to the inventory
     * 
     * @param count Number of new copies to add
     * @return New Inventory instance with updated quantities
     * @throws IllegalArgumentException if count is negative
     */
    public Inventory addCopies(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count to add cannot be negative");
        }

        return new Inventory(
                totalQuantity + count,
                availableQuantity + count,
                borrowedQuantity);
    }

    /**
     * Remove copies from the inventory
     * 
     * @param count Number of copies to remove
     * @return New Inventory instance with updated quantities
     * @throws IllegalArgumentException if count is negative or exceeds available
     *                                  quantity
     */
    public Inventory removeCopies(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count to remove cannot be negative");
        }

        if (count > this.availableQuantity) {
            throw new IllegalArgumentException("Cannot remove more copies than available."
                    + "Available: " + this.availableQuantity);
        }

        return new Inventory(
                totalQuantity - count,
                availableQuantity - count,
                borrowedQuantity);
    }

    /**
     * Check if there are available copies
     * 
     * @return true if availableQuantity > 0, false otherwise
     */
    public boolean hasAvailable() {
        return this.availableQuantity > 0;
    }

    /**
     * Get the borrowed rate as a percentage
     * 
     * @return Borrowed rate (0.0 to 1.0)
     */
    public double getBorrowRate() {
        if (this.totalQuantity == 0) {
            return 0.0;
        }
        return (double) this.borrowedQuantity / this.totalQuantity;
    }

    /**
     * Check if inventory needs reorder based on a threshold
     * 
     * @param threshold Reorder threshold
     * @return true if availableQuantity <= threshold, false otherwise
     */
    public boolean needsReorder(int threshold) {
        return this.availableQuantity <= threshold;
    }
}
