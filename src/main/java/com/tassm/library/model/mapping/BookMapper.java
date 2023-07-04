package com.tassm.library.model.mapping;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Book;

/*
 * This interface defines the methods that should be available for mapping between DTOs and Entity classes
 */

/** Mapper class to convert between Entity and DTO classes */
public interface BookMapper {

    /**
     * Convert between book entity and DTO
     *
     * @param book the book entity
     * @return BookDTO the mapped DTO
     */
    BookDTO bookEntityToDTO(Book book);

    /**
     * Convert between book entity and DTO
     *
     * @param book the book DTO
     * @return Book the book entity representation
     */
    Book bookDTOtoEntity(BookDTO book);

    /**
     * Convert between book DTO entity
     *
     * @param book the createbook DTO
     * @return Book the book entity representation
     */
    Book createBookDtoToEntity(CreateBookDTO book);

    /**
     * Map updates from a DTO to an entity
     *
     * @param dto the DTO from which to apply updates
     * @param entity the entity which updates should be applied to
     */
    void updateBookFromDTO(BookDTO dto, Book entity);

    /**
     * Convert between BookDTO and CreateBookDTO
     *
     * @param bookDto the bookDTO to convert
     * @return CreateBookDTO the createBookDTO
     */
    CreateBookDTO bookDTOToCreateBookDTO(BookDTO bookDto);

    /**
     * Convert between CreateBookDTO and BookDTO
     *
     * @param bookDto the CreateBookDTO to convert
     * @return BookDTO the book DTO
     */
    BookDTO createBookDTO(CreateBookDTO bookDto);
}
