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

    @Query
    Optional<Book> findByIsbn(String isbn);

    @Modifying
    @Query("delete from Book b where b.isbn=:isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
