package com.tassm.library.controller;

import com.tassm.library.exception.BadRequestException;
import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.service.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired BookService bookService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookDTO>> getMany(
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) Integer rangeStart,
            @RequestParam(required = false) Integer rangeEnd) {
        // TODO: this kind of validation is commonly useful and should be rewritten into an aspect
        // and annotation
        if (authorName != null && (rangeStart != null || rangeEnd != null)) {
            throw new BadRequestException(
                    "Only one of author or year range query can be specified!");
        }
        if ((rangeStart != null && rangeEnd == null) || (rangeEnd != null && rangeStart == null)) {
            throw new BadRequestException(
                    "Both rangeStart and rangeEnd must be provided for year range query");
        }
        return ResponseEntity.ok(bookService.findBooks(authorName, rangeStart, rangeEnd));
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

        BookDTO dto = bookService.findBookByIsbn(isbn);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable(name = "isbn") String isbn, @RequestBody @Valid BookDTO dto) {
        dto = bookService.updateBookAndAuthors(isbn, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{isbn}", produces = "application/json")
    public ResponseEntity<Void> deleteByIsbn(@PathVariable(name = "isbn") String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok().body(null);
    }
}
