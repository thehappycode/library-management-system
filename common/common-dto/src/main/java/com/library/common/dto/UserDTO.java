package com.library.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User entity
 * Used for transferring user data between services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should be 10 digits")
    private String phoneNumber;
    
    private String address;
    
    private UserRole role;
    
    private UserStatus status;
    
    private LocalDateTime registeredAt;
    
    private LocalDateTime lastLoginAt;
    
    private String profileImageUrl;
    
    /**
     * User role enumeration
     */
    public enum UserRole {
        ADMIN,
        LIBRARIAN,
        MEMBER,
        GUEST
    }
    
    /**
     * User account status
     */
    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        BLOCKED,
        PENDING_VERIFICATION
    }
}
