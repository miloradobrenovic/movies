package com.example.movies.controller;


import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.service.MovieService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/popular")
    public Page<MovieSummary> getPopularMoviesByPage(
            @RequestParam(defaultValue = "1") int page,
            @Parameter(name = "api_key", required = true)
            @RequestParam String api_key) {
        return movieService.getPopularMovies(page, 50);
    }

    @GetMapping("/{id}")
    public MovieDetails getMovieDetails(@PathVariable Long id,
                                        @Parameter(name = "api_key", required = true)
                                        @RequestParam String api_key) throws Throwable {
        return movieService.getMovieDetails(id);
    }

    @GetMapping("/search")
    public List<MovieSummary> searchMovies(
            @Parameter(description = "Enter whole or part of movie title")
            @RequestParam String query,
            @Parameter(description = "Enter whole or part of movie genre")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Enter lower rating boundary")
            @RequestParam(required = false) Double rating,
            @RequestParam(defaultValue = "rating", required = false)
            @Parameter(description = "Sort by field",
                               schema = @Schema(allowableValues = {"rating", "release_date", "title"}))
            String sortBy,
            @Parameter(name = "api_key", required = true)
            @RequestParam String api_key) {
        return movieService.searchMovies(query, genre, rating, sortBy);
    }

}
