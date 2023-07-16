package mymovie.repository;

import java.util.List;

import mymovie.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    @Query("{ 'genres': { $in: ?0 } }")
    List<Movie> findMoviesByGenres(String[] movieGenres);
}
