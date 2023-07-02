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
import jakarta.validation.ConstraintViolationException;
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
    public List<BookDTO> findAllBooks() {
        List<BookDTO> books = new ArrayList<>();
        bookRepository.findAll().forEach(b -> books.add(bookMapper.bookEntityToDTO(b)));
        return books;
    }

    @Transactional
    public BookDTO saveBookAndAuthors(CreateBookDTO dto) {
        Book book = bookMapper.createBookDtoToEntity(dto);

        if (bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new ResourceConflictException("The book with this ISBN already exists");
        }
        
        Set<Author> authors = new HashSet<>();
        for (String s : dto.getAuthorNames()) {
            var a = authorRepository.findByName(s);
            if (a.isPresent()) {
                authors.add(a.get());
            } else {
                Author newAuthor = new Author();
                newAuthor.setName(s);
                try {
                    newAuthor = authorRepository.save(newAuthor);
                } catch (ConstraintViolationException e) {
                    throw new ResourceConflictException("Author already exists");
                }
                authors.add(newAuthor);
            }
        }
        book.getAuthors().addAll(authors);
        bookRepository.save(book);
        authorRepository.flush();
        bookRepository.flush();
        return bookMapper.bookEntityToDTO(book);
    }

    @Transactional
    public BookDTO findBookAndAuthorsByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        var authors = authorRepository.findAuthorNamesByIsbn(isbn);
        var dto =  bookMapper.bookEntityToDTO(book.get());
        dto.setAuthorNames(authors);
        return dto;
    }

    @Transactional
    public BookDTO updateBook(String isbn, BookDTO updatedBook) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " was not found");
        }
        // should we be able to update an ISBN or not?
        bookMapper.updateBookFromDTO(updatedBook, book.get());
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
}
