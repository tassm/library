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

    @Transactional
    public BookDTO findBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        // var authors = authorRepository.findAuthorNamesByIsbn(isbn);
        var dto = bookMapper.bookEntityToDTO(book.get());
        // dto.setAuthorNames(authors);
        return dto;
    }

    @Transactional
    public BookDTO updateBookAndAuthors(String isbn, BookDTO updatedBook) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        bookMapper.updateBookFromDTO(updatedBook, book.get());
        // update the author records associated with a book
        Set<Author> updatedAuthors = saveAuthorsFromNames(updatedBook.getAuthorNames());
        book.get().setAuthors(updatedAuthors);
        Book res = bookRepository.saveAndFlush(book.get());
        return bookMapper.bookEntityToDTO(res);
    }

    @Transactional
    public void deleteBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        bookRepository.deleteByIsbn(isbn);
        bookRepository.flush();
    }

    public Set<Author> saveAuthorsFromNames(Iterable<String> authorNames) {
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
