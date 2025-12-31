package com.library.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paginated response wrapper
 * Used for endpoints that return paginated data
 *
 * @param <T> The type of content in the page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    /**
     * The list of items in the current page
     */
    private List<T> content;
    
    /**
     * Current page number (0-indexed)
     */
    private int pageNumber;
    
    /**
     * Number of items per page
     */
    private int pageSize;
    
    /**
     * Total number of items across all pages
     */
    private long totalElements;
    
    /**
     * Total number of pages
     */
    private int totalPages;
    
    /**
     * Whether this is the first page
     */
    private boolean first;
    
    /**
     * Whether this is the last page
     */
    private boolean last;
    
    /**
     * Whether there are more pages
     */
    private boolean hasNext;
    
    /**
     * Whether there is a previous page
     */
    private boolean hasPrevious;
    
    /**
     * Check if the page is empty
     */
    public boolean isEmpty() {
        return content == null || content.isEmpty();
    }
}
