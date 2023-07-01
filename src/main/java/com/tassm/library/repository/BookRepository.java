package com.tassm.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tassm.library.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}