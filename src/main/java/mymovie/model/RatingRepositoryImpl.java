package mymovie.model;

import mymovie.repository.RatingRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class RatingRepositoryImpl implements RatingRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public RatingRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Rating> sampleRatingsByUserId(String[] userIds, int size) {
        MatchOperation matchStage = Aggregation.match(Criteria.where("userId").in(userIds));
        SampleOperation sampleStage = Aggregation.sample(size);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);
        AggregationResults<Rating> output = mongoTemplate.aggregate(aggregation, "rating", Rating.class);

        return output.getMappedResults();
    }
}
