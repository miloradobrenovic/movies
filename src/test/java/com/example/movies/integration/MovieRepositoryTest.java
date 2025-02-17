package com.example.movies.integration;

import com.example.movies.model.Movie;
import com.example.movies.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void testFindTop10ByOrderByRatingDesc() {
        // Arrange
        Movie movie1 = new Movie(null, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        Movie movie2 = new Movie(null, "The Dark Knight", LocalDate.of(2008, 7, 18), "url2", "Overview 2", "Action", 9.0, 152, "English");
        movieRepository.saveAll(Arrays.asList(movie1, movie2));

        // Act
        List<Movie> popularMovies = movieRepository.findTop10ByOrderByRatingDesc();

        // Assert
        assertEquals(2, popularMovies.size());
        assertEquals("The Dark Knight", popularMovies.get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Arrange
        Movie movie1 = new Movie(null, "Inception", LocalDate.of(2010, 7, 16), "url1", "Overview 1", "Action", 8.8, 148, "English");
        movieRepository.save(movie1);

        // Act
        List<Movie> searchResults = movieRepository.findByTitleContainingIgnoreCase("Inception");

        // Assert
        assertEquals(1, searchResults.size());
        assertEquals("Inception", searchResults.get(0).getTitle());
    }
}
