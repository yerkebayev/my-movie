package mymovie.repository;

import mymovie.model.Rating;

import java.util.List;

public interface RatingRepositoryCustom {
    public List<Rating> sampleRatingsByUserId(String[] userIds, int size);
}
