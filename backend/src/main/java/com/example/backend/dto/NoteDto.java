package com.example.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class NoteDto {

    private Long id;

    @NotEmpty(message = "Message shouldn't be empty")
    @Size(max = 220, message = "Message shouldn't be longer than 220 characters")
    private String text;
}
