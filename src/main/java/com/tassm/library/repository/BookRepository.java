package com.tassm.library.repository;

import com.tassm.library.model.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    /*
     * NOTE: This is how I intended to retrieve the nested author along with the book
     * For some reason I cannot understand this does not appear to retrieve the authors
     */
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithAuthors(@Param("isbn") String isbn);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
