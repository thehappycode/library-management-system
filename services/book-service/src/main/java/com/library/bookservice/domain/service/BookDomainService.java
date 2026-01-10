package com.library.bookservice.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.library.bookservice.domain.model.Book;

@Service
public class BookDomainService {

    /**
     * Calculates the reorder quantity for a book based on its borrow rate
     * 
     * @param book
     * @return
     */
    public int calculateReorderQuantity(Book book) {
        double borrowRate = book.getInventory().getBorrowRate();
        int currentTotal = book.getInventory().getTotalQuantity();

        if (borrowRate > 0.9) {
            return currentTotal * 2;
        } else if (borrowRate > 0.8) {
            return currentTotal;
        } else if (borrowRate > 0.6) {
            return (int) (currentTotal * 0.5);
        } else {
            return (int) (currentTotal * 0.25);
        }
    }

    /**
     * Recommends similar books based on category and availability
     * 
     * @param book
     * @param allBooks
     * @param limit
     * @return
     */
    public List<Book> recommendSimilarBooks(
            Book book,
            List<Book> allBooks,
            int limit) {

        return allBooks.stream()
                .filter(b -> !b.getId().equals(book.getId())) // Exclude the book itself
                .filter(b -> b.getCategory().equals(book.getCategory())) // Same category
                .filter(Book::isAvailableForBorrowing) // Only available books
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calculates a popularity score for a book based on borrow rate and borrow
     * count
     * 
     * @param book
     * @return
     */
    public double calculatePopularityScore(Book book) {

        double borrowRate = book.getInventory().getBorrowRate();
        int borrowCount = book.getInventory().getBorrowedQuantity();

        // Score = (borrow rate * 70%) + (borrowed count normalized * 30%)
        double borrowRateScore = borrowRate * .7; // Weightage of 70%
        double borrowCountScore = Math.min(borrowCount / 10.0, 1.0) * .3; // Weightage of 30%, normalized

        return borrowRateScore + borrowCountScore;
    }

    /**
     * Determines if a book is trending based on its popularity score
     * 
     * @param book
     * @return
     */
    public boolean isTrending(Book book) {
        double popularityScore = calculatePopularityScore(book);
        return popularityScore > 0.75 && book.isAvailableForBorrowing();
    }

    /**
     * Determines if a book can be archived based on its inventory status
     * 
     * @param book
     * @return
     */
    public boolean canBeArchived(Book book) {

        return book.getInventory().getTotalQuantity() == 0
                && book.getInventory().getBorrowedQuantity() < .1;
    }
}
