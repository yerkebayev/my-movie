package mymovie.model;

import org.springframework.data.annotation.Id;

public class Imdb {
    @Id
    public String id;

    public String movieId;
    public String imdb;

    public Imdb () {}

    public Imdb(
            String movieId,
            String imdb
    ) {
        this.imdb = imdb;
        this.movieId = movieId;
    }

    public void setMovieId (String movieId) {
        this.movieId = movieId;
    }

    public void setImdb (String imdb) {
        this.imdb = imdb;
    }
}
