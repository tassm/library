package com.tassm.library.model.mapping;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Book;

/** Mapper class to convert between Entity and DTO classes */
public interface BookMapper {

    BookDTO bookEntityToDTO(Book book);

    Book bookDTOtoEntity(BookDTO book);

    Book createBookDtoToEntity(CreateBookDTO book);

    void updateBookFromDTO(BookDTO dto, Book entity);

    CreateBookDTO bookDTOToCreateBookDTO(BookDTO bookDto);

    BookDTO createBookDTO(CreateBookDTO bookDto);
}
