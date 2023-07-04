package com.tassm.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.entity.Author;
import com.tassm.library.model.entity.Book;
import com.tassm.library.repository.AuthorRepository;
import com.tassm.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/*
 * This class should probably mock the repository layer below it however for the sake of
 * this exercise we will treat it as an integration test using in memory h2 database.
 *
 * NOTE: This is not as extensive as I would like, I would spend more time on this in future and separate the repository tests into their own classes.
 */
@SpringBootTest
@ActiveProfiles("h2")
public class BookServiceTests {

    @Autowired BookRepository bookRepository;

    @Autowired AuthorRepository authorRepository;

    @Autowired BookService bookService;

    final String isbn1 = "978-3-16-148410-1";
    final String isbn2 = "978-3-16-148410-2";
    final String isbn3 = "978-3-16-148410-3";

    @BeforeEach
    private void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        bookRepository.flush();
        authorRepository.flush();
    }

    @Test
    @DisplayName("Test getting all the books by author name")
    public void testGetBooks_filterByAuthor() {
        // save test data
        saveTestData();

        // Call the service method to retrieve the results
        List<BookDTO> result = bookService.findBooks("Author 1", null, null);

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(isbn1, result.get(0).getIsbn());
        assertEquals("Title 1", result.get(0).getTitle());
        assertTrue(result.get(0).getAuthorNames().contains("Author 1"));
        assertTrue(result.get(0).getAuthorNames().contains("Author 2"));
        assertEquals(2021, result.get(0).getPublicationYear());

        assertEquals(isbn2, result.get(1).getIsbn());
        assertEquals("Title 2", result.get(1).getTitle());
        assertTrue(result.get(1).getAuthorNames().contains("Author 1"));
        assertTrue(result.get(1).getAuthorNames().contains("Author 2"));
        assertEquals(2022, result.get(1).getPublicationYear());
    }

    @Test
    @DisplayName("Test getting all the books by year range")
    public void testGetBooks_filterByYear() {
        // save test data
        saveTestData();

        // Call the service method to retrieve the results
        List<BookDTO> result = bookService.findBooks(null, 2015, 2021);

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(isbn1, result.get(0).getIsbn());
        assertEquals("Title 1", result.get(0).getTitle());
        assertTrue(result.get(0).getAuthorNames().contains("Author 1"));
        assertTrue(result.get(0).getAuthorNames().contains("Author 2"));
        assertEquals(2021, result.get(0).getPublicationYear());
    }

    @Test
    @Transactional
    @DisplayName("Test saving a new book")
    public void testSaveNewBook() {
        // create a dummy book dto
        CreateBookDTO book = new CreateBookDTO(isbn3, "a book", Set.of("an author"), 1972);

        // Call the service method to save the new book
        BookDTO dto = bookService.saveBookAndAuthors(book);

        // retrieve the new book straight from the repository
        Optional<Book> res = bookRepository.findByIsbn(isbn3);

        // Verify the results
        assertEquals(isbn3, dto.getIsbn());
        assertEquals("a book", dto.getTitle());
        assertEquals("an author", dto.getAuthorNames().iterator().next());
        assertEquals(1972, dto.getPublicationYear());

        assertEquals(isbn3, res.get().getIsbn());
        assertEquals("a book", res.get().getTitle());
        assertEquals("an author", res.get().getAuthors().iterator().next().getName());
        assertEquals(1972, res.get().getPublicationYear());
    }

    /**
     * This is just one example to test update and is quite lazy, many more tests would be needed
     * for comprehensive testing
     */
    @Test
    @Transactional
    @DisplayName("Test updating a books title")
    public void testUpdateExistingBook() {
        // save test data
        saveTestData();

        // create a dummy book dto
        BookDTO book = new BookDTO(isbn2, "a book", null, null);

        // Call the service method to update the book
        BookDTO dto = bookService.updateBookAndAuthors(isbn2, book);

        // retrieve the new book straight from the repository
        Optional<Book> res = bookRepository.findByIsbn(isbn2);

        // Verify the results of the returned DTO
        assertEquals(isbn2, dto.getIsbn());
        assertEquals("a book", dto.getTitle());
        assertTrue(dto.getAuthorNames().contains("Author 1"));
        assertTrue(dto.getAuthorNames().contains("Author 2"));
        assertEquals(2022, dto.getPublicationYear());

        // this seems kind of lazy I should probably do it another way
        var authorIterator = res.get().getAuthors().iterator();

        // verify results from the entity retrieved
        assertEquals(isbn2, res.get().getIsbn());
        assertEquals("a book", res.get().getTitle());
        assertEquals("Author 2", authorIterator.next().getName());
        assertEquals("Author 1", authorIterator.next().getName());
        assertEquals(2022, res.get().getPublicationYear());
    }

    @Test
    @Transactional
    @DisplayName("Test deleting a book")
    public void testDeleteBook() {
        // save the test data
        saveTestData();

        // check the book exists
        Optional<Book> res = bookRepository.findByIsbn(isbn1);
        assertTrue(res.isPresent());

        // Call the service method to delete the book
        bookService.deleteBook(isbn1);

        // check the book is deleted
        res = bookRepository.findByIsbn(isbn1);
        assertTrue(res.isEmpty());
    }

    @Transactional
    private void saveTestData() {
        // Prepare mock data
        Set<Book> books = new HashSet<>();
        Set<Author> authors = new HashSet<>();
        authors.add(new Author(null, "Author 1", new HashSet<>()));
        authors.add(new Author(null, "Author 2", new HashSet<>()));
        authors = new HashSet<>(authorRepository.saveAll(authors));
        books.add(new Book(null, isbn1, "Title 1", authors, 2021));
        books.add(new Book(null, isbn2, "Title 2", authors, 2022));

        // persist the dummy records in the in memory database
        bookRepository.saveAllAndFlush(books);
        authorRepository.flush();
    }
}
