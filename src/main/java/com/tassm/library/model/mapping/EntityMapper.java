package com.tassm.library.model.mapping;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/** Mapper class to convert between Entity and DTO classes */
@Mapper(componentModel = "spring")
public interface EntityMapper {

    BookDTO bookEntityToDTO(Book book);

    Book bookDTOtoEntity(BookDTO book);

    void updateBookFromDTO(BookDTO dto, @MappingTarget Book entity);
}
