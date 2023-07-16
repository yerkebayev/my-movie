package mymovie.services;

import mymovie.repository.MovieRepository;
import mymovie.repository.RatingRepository;
import mymovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
        "mymovie.model",
        "mymovie.controllers",
        "mymovie.services",
        "mymovie.jobs",
}, exclude = { DataSourceAutoConfiguration.class })
@EnableMongoRepositories(basePackages = {
        "mymovie.model",
})
public class RestServiceApplication extends SpringBootServletInitializer {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
    }
}