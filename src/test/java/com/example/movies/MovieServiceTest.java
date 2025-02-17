package com.example.movies;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.repository.MovieRepository;
import com.example.movies.service.MovieService;
import com.example.movies.util.MovieMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    
    @Mock
    private MovieMapper movieMapper;
    
    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private MovieDetails movieDetails;
    private MovieSummary movieSummary;

    @BeforeEach
    void setUp() {
        movie = new Movie(1L, "Inception", null, "poster.jpg", "Overview", "Sci-Fi", 8.8, 148, "English");
        movieDetails = new MovieDetails("Inception", null, "poster.jpg", "Overview", "Sci-Fi", 8.8, 148, "English");
        movieSummary = new MovieSummary(1L, "Inception", null, "poster.jpg", 8.8);
    }

    @Test
    void getMovieDetailsShouldReturnMovieDetailsWhenMovieExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.convertToMovieDetails(movie)).thenReturn(movieDetails);

        MovieDetails result = movieService.getMovieDetails(1L);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getMovieDetailsShouldThrowExceptionWhenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieDetails(1L));
    }

    @Test
    void getPopularMoviesShouldReturnPagedMovieSummaries() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));

        when(movieRepository.findTop50ByOrderByRatingDesc(pageable)).thenReturn(moviePage);
        when(movieMapper.convertToMovieSummary(movie)).thenReturn(movieSummary);

        Page<MovieSummary> result = movieService.getPopularMovies(1, 50);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchMoviesShouldReturnFilteredMovies() {
        List<Movie> movies = List.of(movie);
        when(movieRepository.searchMovies(any(), any(), any(), any(Sort.class))).thenReturn(movies);
        when(movieMapper.convertToMovieSummary(movie)).thenReturn(movieSummary);

        List<MovieSummary> result = movieService.searchMovies("Inception", "Sci-Fi", 8.0, "rating");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }
}