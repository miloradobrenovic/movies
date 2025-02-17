package com.example.movies.integration;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void testGetPopularMovies() throws Exception {
        // Arrange
        Movie movie1 = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        Movie movie2 = new Movie(2L, "The Dark Knight", LocalDate.of(2008, 7, 18), "url2", "Overview 2", "Action", 9.0, 152, "English");
        when(movieService.getPopularMovies()).thenReturn(Arrays.asList(movie1, movie2)
                .stream().map(this::convertToMovieSummary)
                .collect(Collectors.toList()));

        // Act & Assert
        mockMvc.perform(get("/movies/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[1].title").value("The Dark Knight"));
    }

    @Test
    void testSearchMovies() throws Exception {
        // Arrange
        Movie movie1 = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        when(movieService.searchMovies("Inception"))
                .thenReturn(Arrays.asList(convertToMovieSummary(movie1)));

        // Act & Assert
        mockMvc.perform(get("/movies/search").param("query", "Inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void testGetMovieDetails() throws Exception {
        // Arrange
        Movie movie = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        when(movieService.getMovieDetails(1L)).thenReturn(convertToMovieDetails(movie));

        // Act & Assert
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void testGetMovieDetails_NotFound() throws Exception {
        // Arrange
        when(movieService.getMovieDetails(1L)).thenThrow(new ResourceNotFoundException("Movie not found"));

        // Act & Assert
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isNotFound());
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
