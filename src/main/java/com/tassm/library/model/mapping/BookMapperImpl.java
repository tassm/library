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
 * Here is some mapping code that does the same stuff anyway...
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

        Book.BookBuilder book1 = Book.builder();

        book1.isbn(book.getIsbn());
        book1.publicationYear(book.getPublicationYear());
        book1.title(book.getTitle());

        return book1.build();
    }

    @Override
    public Book createBookDtoToEntity(CreateBookDTO book) {
        if (book == null) {
            return null;
        }

        Book.BookBuilder book1 = Book.builder();

        book1.isbn(book.getIsbn());
        if (book.getPublicationYear() != null) {
            book1.publicationYear(book.getPublicationYear());
        }
        book1.title(book.getTitle());

        return book1.build();
    }

    @Override
    public void updateBookFromDTO(BookDTO dto, Book entity) {
        if (dto == null) {
            return;
        }

        entity.setIsbn(dto.getIsbn());
        entity.setPublicationYear(dto.getPublicationYear());
        entity.setTitle(dto.getTitle());
    }
}
