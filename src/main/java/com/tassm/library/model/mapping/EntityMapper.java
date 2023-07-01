package com.tassm.library.model.mapping;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.entity.Book;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/** Mapper class to convert between Entity and DTO classes */
@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "authorName", ignore = true)
    BookDTO bookEntityToDTO(Book book);

    @InheritInverseConfiguration
    Book bookDTOtoEntity(BookDTO book);

    void updateBookFromDTO(BookDTO dto, @MappingTarget Book entity);
}
