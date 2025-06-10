package io.github.adil_jr.literadyu.repository;

import io.github.adil_jr.literadyu.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}