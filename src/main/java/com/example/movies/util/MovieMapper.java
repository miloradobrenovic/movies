package com.example.movies.util;

import com.example.movies.model.Movie;
import com.example.movies.model.dto.MovieDetails;
import com.example.movies.model.dto.MovieSummary;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    private final ModelMapper modelMapper;

    public MovieMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convert Movie Entity to MovieDetails DTO
    public MovieDetails convertToMovieDetails(Movie movie) {
        return modelMapper.map(movie, MovieDetails.class);
    }

    // Convert Movie Entity to MovieSummary DTO
    public MovieSummary convertToMovieSummary(Movie movie) {
        return modelMapper.map(movie, MovieSummary.class);
    }

    // Convert MovieDetails DTO to Movie Entity
    public Movie convertToEntity(MovieDetails movieDetails) {
        return modelMapper.map(movieDetails, Movie.class);
    }
}