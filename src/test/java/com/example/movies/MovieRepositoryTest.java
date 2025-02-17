package com.example.movies;

import com.example.movies.model.Movie;
import com.example.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie(null, "Inception", LocalDate.of(2020, 11, 18), "poster.jpg", "Overview", "Sci-Fi", 8.8, 148, "English");
        movie = movieRepository.save(movie);
    }

    @Test
    void findByIdShouldReturnMovieWhenMovieExists() {
        Optional<Movie> foundMovie = movieRepository.findById(movie.getId());
        assertTrue(foundMovie.isPresent());
        assertEquals("Inception", foundMovie.get().getTitle());
    }

    @Test
    void findTop50ByOrderByRatingDescShouldReturnMoviesOrderedByRating() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Movie> movies = movieRepository.findTop50ByOrderByRatingDesc(pageable);
        assertFalse(movies.isEmpty());
    }

    @Test
    void findByTitleContainingIgnoreCaseShouldReturnMatchingMovies() {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase("inception");
        assertFalse(movies.isEmpty());
        assertEquals("Inception", movies.get(0).getTitle());
    }

    @Test
    void searchMoviesShouldReturnFilteredMovies() {
        Sort sort = Sort.by("rating").descending();
        List<Movie> movies = movieRepository.searchMovies("Inception", "Sci-Fi", 8.0, sort);
        assertFalse(movies.isEmpty());
        assertEquals("Inception", movies.get(0).getTitle());
    }
}
