package com.tassm.library.model.mapping;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Author;
import com.tassm.library.model.entity.Book;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/*
 * NOTE: For some reason I couldn't get mapstruct to play nice, maybe I configured it wrong?
 * Here is some (partially mapstruct generated) mapping code that does the same stuff anyway.
 */
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDTO bookEntityToDTO(Book book) {
        if (book == null) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();
        Set<String> authorNames = new HashSet<>();

        for (Author a : book.getAuthors()) {
            authorNames.add(a.getName());
        }

        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthorNames(authorNames);

        return bookDTO;
    }

    @Override
    public Book bookDTOtoEntity(BookDTO book) {
        if (book == null) {
            return null;
        }

        Book book1 = new Book();

        book1.setIsbn(book.getIsbn());
        if (book.getPublicationYear() != null) {
            book1.setPublicationYear(book.getPublicationYear());
        }
        book1.setTitle(book.getTitle());
        book1.setAuthors(new HashSet<>());

        return book1;
    }

    @Override
    public Book createBookDtoToEntity(CreateBookDTO book) {
        if (book == null) {
            return null;
        }

        Book book1 = new Book();

        book1.setIsbn(book.getIsbn());
        if (book.getPublicationYear() != null) {
            book1.setPublicationYear(book.getPublicationYear());
        }
        book1.setTitle(book.getTitle());
        book1.setAuthors(new HashSet<>());

        return book1;
    }

    @Override
    public void updateBookFromDTO(BookDTO dto, Book entity) {
        if (dto == null) {
            return;
        }
        if (dto.getIsbn() != null) {
            entity.setIsbn(dto.getIsbn());
        }
        if (dto.getPublicationYear() != null) {
            entity.setPublicationYear(dto.getPublicationYear());
        }
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
    }

    @Override
    public CreateBookDTO bookDTOToCreateBookDTO(BookDTO bookDto) {
        return new CreateBookDTO(
                bookDto.getIsbn(),
                bookDto.getTitle(),
                bookDto.getAuthorNames(),
                bookDto.getPublicationYear());
    }

    @Override
    public BookDTO createBookDTO(CreateBookDTO createBookDTO) {
        return new BookDTO(
                createBookDTO.getIsbn(),
                createBookDTO.getTitle(),
                createBookDTO.getAuthorNames(),
                createBookDTO.getPublicationYear());
    }
}
