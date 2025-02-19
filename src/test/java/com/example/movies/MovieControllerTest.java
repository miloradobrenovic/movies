package com.example.movies;

import com.example.movies.controller.MovieController;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import com.example.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Value("${api.key}")
    private String apiKey;

    @Test
    void getMovieDetailsShouldReturnMovieDetails() throws Exception {
        MovieDetails movieDetails = new MovieDetails("Inception", null, "poster.jpg", "Overview", "Sci-Fi", 8.8, 148, "English");
        when(movieService.getMovieDetails(1L)).thenReturn(movieDetails);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/1")
                        .header("api_key", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void getPopularMoviesShouldReturnMovieSummaries() throws Exception {
        when(movieService.getPopularMovies(1, 50)).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/popular")
                        .header("api_key", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    void searchMoviesShouldReturnFilteredMovies() throws Exception {
        when(movieService.searchMovies(any(), any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/search?query=test")
                        .header("api_key", apiKey))
                .andExpect(status().isOk());
    }
}
