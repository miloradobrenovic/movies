package com.example.movies.controller;


import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.service.MovieService;
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
    public List<MovieSummary> getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getPopularMovies();
    }

    @GetMapping("/popularByPage")
    public Page<MovieSummary> getPopularMoviesByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return movieService.getPopularMovies(page, size);
    }

    @GetMapping("/search")
    public List<MovieSummary> searchMovies(@RequestParam String query) {
        return movieService.searchMovies(query);
    }

    @GetMapping("/{id}")
    public MovieDetails getMovieDetails(@PathVariable Long id) throws Throwable {
        return movieService.getMovieDetails(id);
    }

    @GetMapping("/search2")
    public Page<MovieSummary> searchMoviesWithSortAndFilters(
            @RequestParam String query,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return movieService.searchMoviesWithSortAndFilters(query, genre, sortBy, page, size);
    }

}
