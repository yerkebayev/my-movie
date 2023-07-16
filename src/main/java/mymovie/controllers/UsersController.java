package mymovie.controllers;

import mymovie.model.MovieDetail;
import mymovie.model.Rating;
import mymovie.model.User;
import mymovie.services.MainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/users")
public class UsersController {
    @Autowired
    private MainService mainService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieDetail>> getRecommendations(
            @RequestParam(required = false, defaultValue = "") String gender,
            @RequestParam(required = false, defaultValue = "") String age,
            @RequestParam(required = false, defaultValue = "") String occupation,
            @RequestParam(required = false, defaultValue = "") String genres
    ) throws MainService.IncorrectInputException {
        ArrayList<String> args = new ArrayList<>();

        args.add(gender);
        args.add(age);
        args.add(occupation);
        args.add(genres);

        mainService.validateInput(args.toArray(new String[0]));

        String genderArg = args.get(0);
        String ageArg = args.get(1);
        String occupationArg = args.get(2);
        String movieGenresArg = args.get(3);

        String[] splitMovieCategories = mainService.splitMovieGenres(movieGenresArg);
        List<MovieDetail> filteredMovies = mainService.filteredListOfMoviesWithIMDB(splitMovieCategories);
        List<Integer> similarOccupationsList = mainService.similarOccupationsList(occupationArg);
        List<User> similarUsers = mainService.getUsersFromGivenConstraints(genderArg, ageArg, similarOccupationsList);
        List<Rating> ratings =  mainService.getRatingsFromUsers(similarUsers);
        HashMap<String, Double> movieGenreRankings = mainService.movieGenreRankings(ratings, filteredMovies);
        List<MovieDetail> recommendedMovies = mainService.recommendMovies(filteredMovies, movieGenreRankings);

        return ResponseEntity.ok(recommendedMovies);
    }

    @ExceptionHandler(MainService.IncorrectInputException.class)
    public ResponseEntity<UsersErrorResponse> inputValidationException(
            MainService.IncorrectInputException exception
    ) {
        UsersErrorResponse errorResponse = new UsersErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }
}
