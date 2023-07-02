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

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.authors WHERE name = :authorName")
    Set<Book> findByAuthorName(@Param("authorName") String authorName);

    @Query("SELECT DISTINCT b FROM Book b WHERE b.publicationYear BETWEEN :startYear AND :endYear")
    Set<Book> findBetweenYearRange(@Param("startYear") int start, @Param("endYear") int end);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
