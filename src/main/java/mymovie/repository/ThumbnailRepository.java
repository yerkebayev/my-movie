package mymovie.repository;

import mymovie.model.Thumbnail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends MongoRepository<Thumbnail, String> { }
