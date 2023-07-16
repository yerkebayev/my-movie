package mymovie.model;

import org.springframework.data.annotation.Id;

public class Thumbnail {
    @Id
    public String id;

    public String movieId;
    public String thumbnail;

    public Thumbnail () {}

    public Thumbnail(
            String movieId,
            String thumbnail
    ) {
        this.thumbnail = thumbnail;
        this.movieId = movieId;
    }

    public void setThumbnail (String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setMovieId (String movieId) {
        this.movieId = movieId;
    }
}
