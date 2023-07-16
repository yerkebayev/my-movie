package mymovie.controllers;

import mymovie.model.*;
import mymovie.model.User;
import mymovie.services.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Integer.parseInt;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/movies")
public class MoviesController {
    private final MainService mainService;

    public MoviesController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDetail>> getAllMovies(){
        String[] splitMovieCategories = {};
        List<MovieDetail> movies = mainService.filteredListOfMoviesWithIMDB(splitMovieCategories);

        return ResponseEntity.ok(movies);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieDetail>> getRecommendations(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String limit
    ) {
        String movieTitle = title;

        if (movieTitle == null) {
            movieTitle = "";
        }

        int parsedLimit;

        try {
            if (limit == null || limit.equals("")) {
                parsedLimit = 10;
            } else {
                parsedLimit = parseInt(limit);

                if (parsedLimit < 0) {
                    parsedLimit = 10;
                }
            }
        } catch (NumberFormatException e) {
            parsedLimit = 10;
        }

        String[] splitMovieCategories = {};
        List<MovieDetail> filteredMovies = mainService.filteredListOfMoviesWithIMDB(splitMovieCategories);
        List<Integer> similarOccupationsList = mainService.similarOccupationsList("");
        List<User> similarUsers = mainService.getUsersFromGivenConstraints("", "", similarOccupationsList);
        List<Rating> ratings =  mainService.getRatingsFromUsers(similarUsers);
        List<MovieDetail> recommendedMovies = mainService.recommendMoviesByTitle(movieTitle, filteredMovies, parsedLimit, ratings);

        return ResponseEntity.ok(recommendedMovies);
    }
}
