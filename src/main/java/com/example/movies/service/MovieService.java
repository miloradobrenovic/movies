package com.example.movies.service;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.repository.MovieRepository;
import com.example.movies.util.MovieMapper;
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
    @Autowired
    private MovieMapper movieMapper;

    public Page<MovieSummary> getPopularMovies(int page, int size) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Movie> moviesPage = movieRepository.findTop50ByOrderByRatingDesc(pageable);
            return moviesPage.map(movieMapper::convertToMovieSummary);
    }

    public List<MovieSummary> searchMovies(String query, String genre, Double rating, String sortBy) {

        if (!List.of("rating", "release_date", "title").contains(sortBy.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sortBy parameter. Allowed values: rating, release_date, title");
        }

        Sort sort = ("title").equals(sortBy) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        List<Movie> movies = movieRepository.searchMovies(query, genre, rating, sort);
        return movies.stream()
                .map(movieMapper::convertToMovieSummary)
                .collect(Collectors.toList());
    }

    public MovieDetails getMovieDetails(Long id) throws ResourceNotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Movie not found with id: " + id));
        return movieMapper.convertToMovieDetails(movie);
    }

}
