package com.example.backend.exception.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime dateTime;
    private String message;
    private List<String> details;
    private String path;
}
