package mymovie.model;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.annotation.Id;

public class Movie {
    @Id
    public String id;
    public String movieId;
    public String title;
    public List<String> genres;

    public Movie() {}

    public Movie(
            String movieId,
            String title,
            List<String> genres
    ) {
        this.title = title;
        this.genres = genres;
        this.movieId = movieId;
    }

    public void setGenres (String genres) {
        this.genres = Arrays.asList(genres.toLowerCase().split("\\|"));
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setMovieId (String movieId) {
        this.movieId = movieId;
    }
}
