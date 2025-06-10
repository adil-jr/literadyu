package io.github.adil_jr.literadyu.model;

import io.github.adil_jr.literadyu.dto.BookDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String language;
    private Double downloadCount;

    @ManyToOne
    private Author author;

    public Book() {
    }

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        this.language = bookDTO.languages().isEmpty() ? null : bookDTO.languages().get(0);
        this.downloadCount = bookDTO.downloadCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Double downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book: '" + title + "' (" + language + "), downloads: " + downloadCount +
                ", Author: " + (author != null ? author.getName() : "N/A");
    }
}