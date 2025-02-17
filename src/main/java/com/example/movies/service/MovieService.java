package com.example.movies.service;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieSummary> getPopularMovies() {
        List<Movie> movies =movieRepository.findTop10ByOrderByRatingDesc();
        return movies.stream()
                .map(this::convertToMovieSummary)
                .collect(Collectors.toList());
    }

    public Page<MovieSummary> getPopularMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Movie> moviesPage = movieRepository.findAllByOrderByRatingDesc(pageable);
        return moviesPage.map(this::convertToMovieSummary);
    }

    public List<MovieSummary> searchMovies(String query) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(query);
        return movies.stream()
                .map(this::convertToMovieSummary)
                .collect(Collectors.toList());
    }

    public MovieDetails getMovieDetails(Long id) throws ResourceNotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Movie not found with id: " + id));
        return convertToMovieDetails(movie);
    }

    public Page<MovieSummary> searchMoviesWithSortAndFilters(
            String query, String genre, String sortBy, int page, int size) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Movie> moviesPage = movieRepository.searchMovies(query, genre, pageable);
        return moviesPage.map(this::convertToMovieSummary);
    }

    private MovieDetails convertToMovieDetails(Movie movie) {
        return new MovieDetails(
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getOverview(),
                movie.getGenres(),
                movie.getRating(),
                movie.getRuntime(),
                movie.getLanguage());
    }

    private MovieSummary convertToMovieSummary(Movie movie) {
        return new MovieSummary(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getRating());
    }

}
