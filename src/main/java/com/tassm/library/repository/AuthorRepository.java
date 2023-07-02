package com.tassm.library.repository;

import com.tassm.library.model.entity.Author;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);

    @Query("SELECT DISTINCT a.name FROM Author a LEFT JOIN a.books WHERE isbn = :isbn")
    Set<String> findAuthorNamesByIsbn(@Param("isbn") String isbn);
}
