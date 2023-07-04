package com.tassm.library.repository;

import com.tassm.library.model.entity.Book;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    /**
     * Retrieve all books matching an authors name
     *
     * @param authorName the name of the author
     * @return Set<Book> the collection of matching books
     */
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.authors WHERE name = :authorName")
    Set<Book> findByAuthorName(@Param("authorName") String authorName);

    /**
     * Retrieve all books matching a range of publicationYears
     *
     * @param startYear the start of the year range to search
     * @param endYear the end of the year range to search
     * @return Set<Book> the collection of matching books
     */
    @Query("SELECT DISTINCT b FROM Book b WHERE b.publicationYear BETWEEN :startYear AND :endYear")
    Set<Book> findBetweenYearRange(@Param("startYear") int start, @Param("endYear") int end);

    /**
     * Delete a book by a given ISBN
     *
     * @param isbn the unique ISBN of the book
     */
    @Modifying
    @Query("DELETE FROM Book b WHERE b.isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
