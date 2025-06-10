package io.github.adil_jr.literadyu.main;

import io.github.adil_jr.literadyu.dto.BookDTO;
import io.github.adil_jr.literadyu.dto.GutendexResponseDTO;
import io.github.adil_jr.literadyu.model.Author;
import io.github.adil_jr.literadyu.model.Book;
import io.github.adil_jr.literadyu.repository.AuthorRepository;
import io.github.adil_jr.literadyu.repository.BookRepository;
import io.github.adil_jr.literadyu.service.ApiClient;
import io.github.adil_jr.literadyu.service.DataConverter;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiClient apiClient = new ApiClient();
    private final DataConverter dataConverter = new DataConverter();
    private final String API_BASE_URL = "https://gutendex.com/books/";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        System.out.println("Buscando e salvando livros...");
        searchAndSaveBook();
    }

    private void searchAndSaveBook() {
        System.out.println("Digite o título do livro que deseja buscar: ");
        var bookTitle = scanner.nextLine();

        String searchUrl = API_BASE_URL + "?search=" + URLEncoder.encode(bookTitle, StandardCharsets.UTF_8);
        var jsonResponse = apiClient.fetchData(searchUrl);
        GutendexResponseDTO apiData = dataConverter.getData(jsonResponse, GutendexResponseDTO.class);

        if (apiData == null || apiData.results().isEmpty()) {
            System.out.println("Nenhum livro encontrado com este título");
            return;
        }

        BookDTO bookDTO = apiData.results().get(0);

        Optional<Book> existingBook = bookRepository.findByTitleContainingIgnoreCase(bookDTO.title());
        if (existingBook.isPresent()) {
            System.out.println("Este livro já está cadastrado no banco de dados.");
            return;
        }

        Author author;
        if (bookDTO.authors() != null && !bookDTO.authors().isEmpty()) {
            var authorDTO = bookDTO.authors().get(0);
            Optional<Author> existingAuthor = authorRepository.findByNameContainingIgnoreCase(authorDTO.name());
            author = existingAuthor.orElseGet(() -> authorRepository.save(new Author(authorDTO)));
        } else {
            System.out.println("Livro sem autor, não será salvo.");
            return;
        }

        Book newBook = new Book(bookDTO);
        newBook.setAuthor(author);
        bookRepository.save(newBook);

        System.out.println("Livro salvo com sucesso!");
        System.out.println(newBook);
    }
}