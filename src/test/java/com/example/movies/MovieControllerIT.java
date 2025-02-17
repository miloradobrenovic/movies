package com.example.movies;

import com.example.movies.model.Movie;
import com.example.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MovieControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie;

    @Value("${api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        testMovie = new Movie(1L, "Inception", LocalDate.of(2010, 7, 16), "poster.jpg", "Overview", "Sci-Fi", 8.8, 148, "English");
        movieRepository.save(testMovie);
    }

    @Test
    void getMovieDetailsShouldReturnMovieDetails() throws Exception {
        mockMvc.perform(get("/movies/" + testMovie.getId() + "?api_key=" + apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void getPopularMoviesShouldReturnMovieSummaries() throws Exception {
        mockMvc.perform(get("/movies/popular?api_key=" + apiKey))
                .andExpect(status().isOk());
    }

    @Test
    void searchMoviesShouldReturnFilteredMovies() throws Exception {
        mockMvc.perform(get("/movies/search?query=Inception&api_key=" + apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void getMovieDetailsShouldReturnNotFoundWhenMovieDoesNotExist() throws Exception {
        mockMvc.perform(get("/movies/9999?api_key=" + apiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMovieDetailsShouldReturnBadRequestWhenApiKeyIsMissing() throws Exception {
        mockMvc.perform(get("/movies/" + testMovie.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void searchMoviesShouldReturnBadRequestWhenQueryIsMissing() throws Exception {
        mockMvc.perform(get("/movies/search?api_key=" + apiKey))
                .andExpect(status().isBadRequest());
    }
}
