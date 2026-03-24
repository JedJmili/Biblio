package com.bibliotech.mapper;

import com.bibliotech.dto.BookCreateDTO;
import com.bibliotech.dto.BookDTO;
import com.bibliotech.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    @Mapping(target = "authorName", expression = "java(book.getAuthor() != null ? book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName() : null)")
    @Mapping(target = "categoryName", expression = "java(book.getCategory() != null ? book.getCategory().getName() : null)")
    BookDTO toDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "availableCopies", source = "totalCopies")
    Book toEntity(BookCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(BookCreateDTO dto, @MappingTarget Book book);
}
