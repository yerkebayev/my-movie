package mymovie.model;

import java.util.List;

public class MovieDetail {
    public String movieId;
    public String title;
    public List<String> genres;
    public String imdb;
    public String thumbnail;

    public MovieDetail(
            String movieId,
            String title,
            List<String> genres,
            String imdb,
            String thumbnail
    ) {
        this.title = title;
        this.genres = genres;
        this.imdb = imdb;
        this.thumbnail = thumbnail;
        this.movieId = movieId;
    }
}
