package com.tassm.library.controller;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.service.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired BookService bookService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookDTO>> getMany() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<BookDTO> create(@RequestBody @Valid CreateBookDTO dto) {
        BookDTO created = bookService.saveBookAndAuthors(dto);
        URI uri = URI.create("/book/" + created.getIsbn());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<BookDTO> getByIsbn(
            @Valid @ISBN @PathVariable(name = "isbn") String isbn) {

        BookDTO dto = bookService.findBookAndAuthorsByIsbn(isbn);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable(name = "isbn") String isbn, @RequestBody @Valid BookDTO dto) {
        dto = bookService.updateBook(isbn, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<Void> deleteByIsbn(@PathVariable(name = "isbn") String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok().body(null);
    }
}
