package mymovie.model;

import org.springframework.data.annotation.Id;

public class Rating {
    @Id
    public String id;

    public String userId;
    public String movieId;
    public double rating;

    public Rating () {}

    public Rating(
            String userId,
            String movieId,
            double rating
    ) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public void setMovieId (String movieId) {
        this.movieId = movieId;
    }

    public void setRating (String rating) {
        this.rating = Float.parseFloat(rating);
    }

    public void setTimestamp (String timestamp) {
        return;
    }
}
