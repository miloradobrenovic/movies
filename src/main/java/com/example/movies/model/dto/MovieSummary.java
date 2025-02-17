package com.example.movies.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSummary {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private String posterUrl;
    private Double rating;
}
