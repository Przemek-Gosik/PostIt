package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Text can't be empty!")
    @Size(max = 200,message = "Text can't be longer than 200 characters!")
    private String text;
}
