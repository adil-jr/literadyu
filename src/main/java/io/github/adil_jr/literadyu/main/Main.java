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
import java.util.DoubleSummaryStatistics;
import java.util.List;
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
        var option = -1;
        while (option != 0) {
            var menu = """
                    *** LiterAdyu - Seu Catálogo de Livros ***
                    
                    Escolha uma opção pelo número:
                    1- Buscar livro pelo título (e salvar no DB)
                    2- Listar livros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                    
                    --- Opções Avançadas ---
                    6- Top 10 livros mais baixados
                    7- Buscar autor por nome
                    8- Exibir estatísticas de downloads
                    9- Listar autores por ano de nascimento
                    
                    0- Sair
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchAndSaveBook();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listLivingAuthorsInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 6:
                    listTop10Books();
                    break;
                case 7:
                    findAuthorByName();
                    break;
                case 8:
                    showDownloadStatistics();
                    break;
                case 9:
                    listAuthorsByBirthYear();
                    break;
                case 0:
                    System.out.println("Saindo... Obrigado por usar o LiterAdyu!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
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

    private void listRegisteredBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("---- LIVROS REGISTRADOS ----");
            books.forEach(System.out::println);
            System.out.println("-----------------------------");
        }
    }

    private void listRegisteredAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
        } else {
            System.out.println("----- AUTORES REGISTRADOS -----");
            authors.forEach(System.out::println);
            System.out.println("------------------------------");
        }
    }

    private void listLivingAuthorsInYear() {
        System.out.println("Digite o ano para buscar autores vivos:");
        var year = scanner.nextInt();
        scanner.nextLine();
        List<Author> authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado para o ano " + year + ".");
        } else {
            System.out.println("----- AUTORES VIVOS EM " + year + " -----");
            authors.forEach(System.out::println);
            System.out.println("-----------------------------------");
        }
    }

    private void listBooksByLanguage() {
        var languageMenu = """
                Escolha um idioma para a busca:
                es - Espanhol
                en - Inglês
                fr - Francês
                pt - Português
                """;
        System.out.println(languageMenu);
        var lang = scanner.nextLine();
        List<Book> books = bookRepository.findByLanguage(lang);

        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma '" + lang + "'.");
        } else {
            System.out.println("----- LIVROS EM '" + lang.toUpperCase() + "' -----");
            books.forEach(System.out::println);
            System.out.println("--------------------------");
        }
    }

    private void listTop10Books() {
        List<Book> top10Books = bookRepository.findTop10ByOrderByDownloadCountDesc();
        if (top10Books.isEmpty()) {
            System.out.println("Nenhum livro encontrado para gerar o Top 10.");
        } else {
            System.out.println("----- TOP 10 LIVROS MAIS BAIXADOS -----");
            top10Books.forEach(System.out::println);
            System.out.println("---------------------------------------");
        }
    }

    private void findAuthorByName() {
        System.out.println("Digite o nome do autor que deseja buscar:");
        var authorName = scanner.nextLine();
        Optional<Author> author = authorRepository.findByNameContainingIgnoreCase(authorName);
        if(author.isPresent()) {
            System.out.println("----- AUTOR ENCONTRADO -----");
            System.out.println(author.get());
            // Como usamos FetchType.EAGER, os livros já foram carregados.
            System.out.println("Livros deste autor:");
            author.get().getBooks().forEach(book -> System.out.println("  - " + book.getTitle()));
            System.out.println("----------------------------");
        } else {
            System.out.println("Nenhum autor encontrado com o nome '" + authorName + "'.");
        }
    }

    private void showDownloadStatistics() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) {
            System.out.println("Não há livros no banco para gerar estatísticas.");
            return;
        }

        DoubleSummaryStatistics stats = books.stream()
                .mapToDouble(Book::getDownloadCount)
                .summaryStatistics();

        System.out.println("----- ESTATÍSTICAS DE DOWNLOADS -----");
        System.out.printf("Total de Livros: %d\n", stats.getCount());
        System.out.printf("Total de Downloads: %.0f\n", stats.getSum());
        System.out.printf("Média de Downloads por Livro: %.2f\n", stats.getAverage());
        System.out.printf("Maior Número de Downloads: %.0f\n", stats.getMax());
        System.out.printf("Menor Número de Downloads: %.0f\n", stats.getMin());
        System.out.println("-------------------------------------");
    }

    private void listAuthorsByBirthYear() {
        System.out.println("Digite o ano de nascimento para buscar os autores:");
        var year = scanner.nextInt();
        scanner.nextLine();
        List<Author> authors = authorRepository.findByBirthYear(year);
        if(authors.isEmpty()){
            System.out.println("Nenhum autor encontrado nascido no ano de " + year + ".");
        } else {
            System.out.println("----- AUTORES NASCIDOS EM " + year + " -----");
            authors.forEach(System.out::println);
            System.out.println("-----------------------------------");
        }
    }
}