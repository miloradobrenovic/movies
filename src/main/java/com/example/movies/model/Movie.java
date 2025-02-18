package com.example.movies.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(nullable = false, length = 1000)
    private String overview;

    @Column(nullable = false)
    private String genres;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Integer runtime;

    @Column(nullable = false)
    private String language;
}
