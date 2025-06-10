package io.github.adil_jr.literadyu.repository;

import io.github.adil_jr.literadyu.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainingIgnoreCase(String title);
}