package com.tassm.library.service;

import com.tassm.library.exception.ResourceConflictException;
import com.tassm.library.exception.ResourceNotFoundException;
import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Author;
import com.tassm.library.model.entity.Book;
import com.tassm.library.model.mapping.BookMapper;
import com.tassm.library.repository.AuthorRepository;
import com.tassm.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;
    @Autowired BookMapper bookMapper;

    /**
     * Find books by either author name or by a range of publication years If publicationYear ranges
     * are used then both must be provided. Filtering by both author and publication year is NOT
     * IMPLEMENTED
     *
     * @param authorName the name of the author to filter by
     * @param rangeStart the start of the publicationYear range
     * @param rangeEnd the end of the publicationYear range
     * @return
     */
    @Transactional
    public List<BookDTO> findBooks(String authorName, Integer rangeStart, Integer rangeEnd) {
        List<BookDTO> books = new ArrayList<>();
        if (authorName != null) {
            bookRepository
                    .findByAuthorName(authorName)
                    .forEach(b -> books.add(bookMapper.bookEntityToDTO(b)));
        } else if (rangeStart != null) {
            bookRepository
                    .findBetweenYearRange(rangeStart, rangeEnd)
                    .forEach(b -> books.add(bookMapper.bookEntityToDTO(b)));
        }
        return books;
    }

    /**
     * Save a book and associated authors in the database. Saves authors and then saves books with a
     * reference to the persisted authors.
     *
     * @param dto the DTO representing the new book to save
     * @return BookDTO representing the saved book
     */
    @Transactional
    public BookDTO saveBookAndAuthors(CreateBookDTO dto) {
        Book book = bookMapper.createBookDtoToEntity(dto);

        if (bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new ResourceConflictException("The book with this ISBN already exists");
        }
        Set<Author> authors = saveAuthorsFromNames(dto.getAuthorNames());
        book.getAuthors().addAll(authors);
        bookRepository.saveAndFlush(book);
        return bookMapper.bookEntityToDTO(book);
    }

    /**
     * Retrieve a book by its unique ISBN, throws a ResourceNotFoundException if it does not exist.
     *
     * @param isbn unique ISBN of the book
     * @return BookDTO representing the matching book
     */
    @Transactional
    public BookDTO findBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        var dto = bookMapper.bookEntityToDTO(book.get());
        return dto;
    }

    /**
     * Update a book by its unique ISBN, throws a ResourceNotFoundException if it does not exist.
     *
     * @param isbn unique ISBN of the book to update
     * @param updatedBook DTO including any fields which are to be updated
     * @return BookDTO representing the updated book
     */
    @Transactional
    public BookDTO updateBookAndAuthors(String isbn, BookDTO updatedBook) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        bookMapper.updateBookFromDTO(updatedBook, book.get());
        // update the author records associated with a book
        if (updatedBook.getAuthorNames() != null) {
            Set<Author> updatedAuthors = saveAuthorsFromNames(updatedBook.getAuthorNames());
            book.get().setAuthors(updatedAuthors);
        }
        Book res = bookRepository.saveAndFlush(book.get());
        return bookMapper.bookEntityToDTO(res);
    }

    /**
     * Delete a book by its unique ISBN, throws a ResourceNotFoundException if it does not exist.
     *
     * @param isbn unique ISBN of the book to delete
     */
    @Transactional
    public void deleteBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        bookRepository.deleteByIsbn(isbn);
        bookRepository.flush();
    }

    private Set<Author> saveAuthorsFromNames(Iterable<String> authorNames) {
        Set<Author> authors = new HashSet<>();
        for (String s : authorNames) {
            var a = authorRepository.findByName(s);
            if (a.isPresent()) {
                authors.add(a.get());
            } else {
                Author newAuthor = new Author();
                newAuthor.setName(s);
                newAuthor = authorRepository.save(newAuthor);
                authors.add(newAuthor);
            }
        }
        authorRepository.flush();
        return authors;
    }
}
