package com.example.movies.repository;

import com.example.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop10ByOrderByRatingDesc();

    Page<Movie> findAllByOrderByRatingDesc(Pageable pageable);

    List<Movie> findByTitleContainingIgnoreCase(String query);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "AND (:genre IS NULL OR LOWER(m.genres) LIKE LOWER(CONCAT('%', :genre, '%')))")
    Page<Movie> searchMovies(
            @Param("query") String query,
            @Param("genre") String genre,
            Pageable pageable);

}
