package com.library.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * Utility class for validation operations
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Check if object is null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    
    /**
     * Check if object is not null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    /**
     * Require non-null object
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }
    
    /**
     * Check if collection is null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Check if collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Check if map is null or empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * Check if map is not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * Check if array is null or empty
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    
    /**
     * Check if array is not empty
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
    
    /**
     * Check if number is positive
     */
    public static boolean isPositive(Number number) {
        return number != null && number.doubleValue() > 0;
    }
    
    /**
     * Check if number is negative
     */
    public static boolean isNegative(Number number) {
        return number != null && number.doubleValue() < 0;
    }
    
    /**
     * Check if number is zero
     */
    public static boolean isZero(Number number) {
        return number != null && number.doubleValue() == 0;
    }
    
    /**
     * Check if number is within range (inclusive)
     */
    public static boolean isInRange(Number number, Number min, Number max) {
        if (number == null || min == null || max == null) {
            return false;
        }
        double value = number.doubleValue();
        return value >= min.doubleValue() && value <= max.doubleValue();
    }
    
    /**
     * Validate that condition is true
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Validate that condition is false
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Validate state
     */
    public static void state(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
