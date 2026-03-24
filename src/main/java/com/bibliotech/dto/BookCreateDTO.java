package com.bibliotech.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private Long authorId;

    private Long categoryId;

    private String description;

    @Min(value = 1000, message = "Publication year must be valid")
    private int publicationYear;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;
}
