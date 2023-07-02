package com.tassm.library.service;

import com.tassm.library.exception.ResourceConflictException;
import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Author;
import com.tassm.library.model.entity.Book;
import com.tassm.library.model.mapping.BookMapper;
import com.tassm.library.repository.AuthorRepository;
import com.tassm.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;
    @Autowired BookMapper bookMapper;

    @Transactional
    public BookDTO saveBookAndAuthors(CreateBookDTO dto) {
        Book book = bookMapper.createBookDtoToEntity(dto);

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

        try {
            bookRepository.save(book);
        } catch (ConstraintViolationException e) {
            throw new ResourceConflictException("The book with this ISBN already exists");
        }
        authorRepository.flush();
        bookRepository.flush();
        return bookMapper.bookEntityToDTO(book);
    }
}
