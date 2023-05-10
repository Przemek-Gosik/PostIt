package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoteDto {

    private Long id;

    @NotBlank
    @Size(max = 220, message = "Message shouldn't be longer than 220 characters")
    private String text;
}
