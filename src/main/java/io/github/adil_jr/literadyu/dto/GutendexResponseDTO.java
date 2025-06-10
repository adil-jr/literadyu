package io.github.adil_jr.literadyu.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponseDTO(
        @JsonAlias("results") List<BookDTO> results
) {}
