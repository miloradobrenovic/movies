package com.example.movies.unit;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.repository.MovieRepository;
import com.example.movies.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPopularMovies() {
        // Arrange
        Movie movie1 = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        Movie movie2 = new Movie(2L, "The Dark Knight", LocalDate.of(2008, 7, 18), "url2", "Overview 2", "Action", 9.0, 152, "English");
        when(movieRepository.findTop10ByOrderByRatingDesc()).thenReturn(Arrays.asList(movie1, movie2));
// Act
        List<MovieSummary> popularMovies = movieService.getPopularMovies();

        // Assert
        assertEquals(2, popularMovies.size());
        assertEquals("Inception", popularMovies.get(0).getTitle());
        verify(movieRepository, times(1)).findTop10ByOrderByRatingDesc();
    }

    @Test
    void testSearchMovies() {
        // Arrange
        Movie movie1 = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        when(movieRepository.findByTitleContainingIgnoreCase("Inception")).thenReturn(Arrays.asList(movie1));

        // Act
        List<MovieSummary> searchResults = movieService.searchMovies("Inception");

        // Assert
        assertEquals(1, searchResults.size());
        assertEquals("Inception", searchResults.get(0).getTitle());
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase("Inception");
    }

    @Test
    void testGetMovieDetails() throws ResourceNotFoundException {
        // Arrange
        Movie movie = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Act
        MovieDetails movieDetails = movieService.getMovieDetails(1L);

        // Assert
        assertNotNull(movieDetails);
        assertEquals("Inception", movieDetails.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieDetails_NotFound() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> movieService.getMovieDetails(1L));
        verify(movieRepository, times(1)).findById(1L);
    }
}
