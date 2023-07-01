package com.tassm.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tassm.library.model.entity.Book;
import com.tassm.library.repository.AuthorRepository;
import com.tassm.library.repository.BookRepository;

@Service
public class BookService {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;

    public List<Book> saveBookAndAuthors(Book book){
        // do authors exists? if so retrieve?
        // save and flush all authors
        // save and flush book
    }
}
