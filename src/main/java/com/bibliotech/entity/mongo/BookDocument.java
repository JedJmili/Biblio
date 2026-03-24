package com.bibliotech.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDocument {

    @Id
    private String id;

    private String title;

    @Indexed(unique = true)
    private String isbn;

    private String authorName;
    private String categoryName;
    private String description;
    private int publicationYear;
    private int totalCopies;
    private int availableCopies;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
