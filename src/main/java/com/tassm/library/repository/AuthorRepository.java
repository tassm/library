package com.tassm.library.repository;

import com.tassm.library.model.entity.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find and author by their full name
     *
     * @param name the name of the author
     * @return Optional<Author> the author with the matching name
     */
    Optional<Author> findByName(String name);
}
