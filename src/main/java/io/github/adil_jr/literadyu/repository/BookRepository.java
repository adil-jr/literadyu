package io.github.adil_jr.literadyu.repository;

import io.github.adil_jr.literadyu.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByLanguage(String language);

    List<Book> findTop10ByOrderByDownloadCountDesc();
}