package com.tassm.library.controller;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.entity.Book;
import com.tassm.library.model.mapping.EntityMapper;
import com.tassm.library.repository.AuthorRepository;
import com.tassm.library.repository.BookRepository;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;
    @Autowired EntityMapper mapper;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookDTO>> getMany(@RequestParam(name = "isbn") String isbn) {
        List<BookDTO> books = new ArrayList<>();
        bookRepository.findAll().forEach(b -> books.add(mapper.bookEntityToDTO(b)));
        return ResponseEntity.ok(books);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto) {
        Book book = mapper.bookDTOtoEntity(dto);
        try {
            bookRepository.saveAndFlush(book);
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("there was a key conflict");
        }
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<BookDTO> getByIsbn(@PathVariable(name = "isbn") String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new RuntimeException("a problem");
        }
        BookDTO dto = mapper.bookEntityToDTO(book.get());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable(name = "isbn") String isbn, @RequestBody BookDTO dto) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new RuntimeException("a problem");
        }
        mapper.updateBookFromDTO(dto, book.get());
        bookRepository.saveAndFlush(book.get());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<Void> deleteByIsbn(@PathVariable(name = "isbn") String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new RuntimeException("lol");
        }
        bookRepository.deleteByIsbn(isbn);
        bookRepository.flush();
        return ResponseEntity.ok().body(null);
    }
}
