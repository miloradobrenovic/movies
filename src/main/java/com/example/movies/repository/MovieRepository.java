package com.example.movies.repository;

import com.example.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findTop50ByOrderByRatingDesc(Pageable pageable);

    List<Movie> findByTitleContainingIgnoreCase(String query);

    @Query(value = "SELECT * FROM movies WHERE " +
            "LOWER(title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
            "(:genre IS NULL OR LOWER(genres) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
            "(:rating IS NULL OR rating >= :rating)",
            nativeQuery = true)
    List<Movie> searchMovies(
            @Param("query") String query,
            @Param("genre") String genre,
            @Param("rating") Double rating,
            Sort sort);
}
