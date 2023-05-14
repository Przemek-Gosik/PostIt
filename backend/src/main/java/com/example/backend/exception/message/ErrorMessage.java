package com.example.backend.exception.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Class of object to return with error response
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class ErrorMessage {
    private int statusCode;
    private LocalDateTime dateTime;
    private String message;
    private String description;
}
