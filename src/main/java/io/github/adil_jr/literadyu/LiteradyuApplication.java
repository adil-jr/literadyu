package io.github.adil_jr.literadyu;

import io.github.adil_jr.literadyu.dto.GutendexResponseDTO;
import io.github.adil_jr.literadyu.service.ApiClient;
import io.github.adil_jr.literadyu.service.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteradyuApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LiteradyuApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ApiClient apiClient = new ApiClient();
        DataConverter dataConverter = new DataConverter();

        final String API_ADDRESS = "https://gutendex.com/books/";

        var jsonResponse = apiClient.fetchData(API_ADDRESS);
        System.out.println("---- JSON Recebido ----");
        System.out.println(jsonResponse);

        GutendexResponseDTO apiData = dataConverter.getData(jsonResponse, GutendexResponseDTO.class);
        System.out.println("\n---- Dados Convertidos ----");
        System.out.println(apiData);

        if (apiData != null && !apiData.results().isEmpty()) {
            System.out.println("\n---- Título do Primeiro Livro ----");
            System.out.println(apiData.results().get(0).title());
        }
    }
}