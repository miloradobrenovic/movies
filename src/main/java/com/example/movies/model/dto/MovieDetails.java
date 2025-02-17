package com.example.movies.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MovieDetails {
    private String title;
    private LocalDate releaseDate;
    private String posterUrl;
    private String overview;
    private String genres;
    private Double rating;
    private Integer runtime;
    private String language;
}
