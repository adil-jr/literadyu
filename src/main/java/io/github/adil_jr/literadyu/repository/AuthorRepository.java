package io.github.adil_jr.literadyu.repository;

import io.github.adil_jr.literadyu.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {}