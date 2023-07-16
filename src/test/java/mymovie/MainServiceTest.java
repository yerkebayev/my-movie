package mymovie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import static org.junit.Assert.*;
import java.util.Arrays;

import mymovie.model.Rating;
import mymovie.model.MovieDetail;
import mymovie.services.MainService;

public class MainServiceTest {
    private MainService mainService = new MainService();

    @Test
    public void testValidateInputWithVariousArgc () {
        String[] args1 = {"", ""};
        String[] args2 = {"", "", "", "", ""};
        String[] args3 = {"", "", ""};
        String[] args4 = {"", "", "", ""};

        try {
            mainService.validateInput(args1);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect argc", e.getMessage());
        }

        try {
            mainService.validateInput(args2);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect argc", e.getMessage());
        }

        try {
            mainService.validateInput(args3);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect argc", e.getMessage());
        }

        try {
            mainService.validateInput(args4);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect argc", e.getMessage());
        }
    }

    @Test
    public void testValidateInputWithVariousGender () {
        String[] args1 = {"Random Word", "23", "", "Drama"};
        String[] args2 = {"F", "", ""};
        String[] args3 = {"M", "", ""};
        String[] args4 = {"", "", ""};
        String[] args5 = {null, "", ""};

        try {
            mainService.validateInput(args1);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect gender format", e.getMessage());
        }

        try {
            mainService.validateInput(args2);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect gender format", e.getMessage());
        }

        try {
            mainService.validateInput(args3);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect gender format", e.getMessage());
        }

        try {
            mainService.validateInput(args4);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect gender format", e.getMessage());
        }

        try {
            mainService.validateInput(args5);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("One of the args is not specified", e.getMessage());
        }
    }

    @Test
    public void testValidateInputWithVariousAge () {
        String[] args1 = {"F", "Random Word", ""};
        String[] args2 = {"M", "-2", ""};
        String[] args3 = {"", "15", ""};
        String[] args4 = {"", "125", "", ""};
        String[] args5 = {"", "", "", ""};
        String[] args6 = {"F", null, ""};

        try {
            mainService.validateInput(args1);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect age format", e.getMessage());
        }

        try {
            mainService.validateInput(args2);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect age format", e.getMessage());
        }

        try {
            mainService.validateInput(args3);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect age format", e.getMessage());
        }

        try {
            mainService.validateInput(args4);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect age format", e.getMessage());
        }

        try {
            mainService.validateInput(args5);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect age format", e.getMessage());
        }

        try {
            mainService.validateInput(args6);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("One of the args is not specified", e.getMessage());
        }
    }

    @Test
    public void testValidateInputWithVariousMovieCategories () {
        String[] args1 = {"", "", "", "AdveNturE,ComEdy"};
        String[] args2 = {"", "", "", "Adventure , Comedy"};
        String[] args3 = {"", "", "", "Random category"};
        String[] args4 = {"", "", "", "DraMa,Sci-Fi,HorRor"};
        String[] args5 = {"", "", "", ""};
        String[] args6 = {"", "", "", null};

        try {
            mainService.validateInput(args1);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect movie genres format", e.getMessage());
        }

        try {
            mainService.validateInput(args2);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect movie genres format", e.getMessage());
        }

        try {
            mainService.validateInput(args3);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("Incorrect movie genres format", e.getMessage());
        }

        try {
            mainService.validateInput(args4);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect movie genres format", e.getMessage());
        }

        try {
            mainService.validateInput(args5);
            assertTrue(true);
        } catch (MainService.IncorrectInputException e) {
            assertNotEquals("Incorrect movie genres format", e.getMessage());
        }

        try {
            mainService.validateInput(args6);
            assertTrue(false);
        } catch (MainService.IncorrectInputException e) {
            assertEquals("One of the args is not specified", e.getMessage());
        }
    }

    @Test
    public void testGetOccupationId(){
        String arg1 = "footballer", arg2 = "writer", arg3 = "doctor";

        assertEquals(0, MainService.getOccupationId(arg1));
        assertEquals(20, MainService.getOccupationId(arg2));
        assertEquals(6, MainService.getOccupationId(arg3));
    }

    @Test
    public void testSimilarOccupationsList(){
        String arg1 = "footballer", arg2 = "retired", arg3 = "scientist", arg4 = "managerial", arg5 = "";
        ArrayList<Integer> expected1 = new ArrayList<Integer>();
        expected1.add(0);

        ArrayList<Integer> expected2 = new ArrayList<Integer>();
        expected2.add(13);
        expected2.add(19);


        ArrayList<Integer> expected3 = new ArrayList<Integer>();
        expected3.add(12);
        expected3.add(15);

        ArrayList<Integer> expected4 = new ArrayList<Integer>();
        expected4.add(3);
        expected4.add(7);

        ArrayList<Integer> expected5 = new ArrayList<Integer>();

        for(int i = 0; i <= 20; i++){
            expected5.add(i);
        }

        assertEquals(expected1, mainService.similarOccupationsList(arg1));
        assertEquals(expected2, mainService.similarOccupationsList(arg2));
        assertEquals(expected3, mainService.similarOccupationsList(arg3));
        assertEquals(expected4, mainService.similarOccupationsList(arg4));
        assertEquals(expected5, mainService.similarOccupationsList(arg5));
    }

    @Test
    public void testMovieGenreRankings () {
        List<Rating> ratings = new ArrayList<>();

        ratings.add(new Rating("1", "2028", 5));
        ratings.add(new Rating("1", "531", 4));
        ratings.add(new Rating("1", "1246", 4));
        ratings.add(new Rating("192", "3810", 2));

        List<MovieDetail> movies = new ArrayList<>();

        List<String> movie1Genres = Arrays.asList("Action|Drama|War".toLowerCase().split("\\|"));
        List<String> movie2Genres = Arrays.asList("Children's|Drama".toLowerCase().split("\\|"));
        List<String> movie3Genres = Arrays.asList("Drama".toLowerCase().split("\\|"));

        movies.add(new MovieDetail("2028", "Saving Private Ryan (1998)", movie1Genres, "", ""));
        movies.add(new MovieDetail("531", "Secret Garden, The (1993)", movie2Genres, "", ""));
        movies.add(new MovieDetail("1246", "Dead Poets Society (1989)", movie3Genres, "", ""));

        final HashMap<String, Double> movieGenreRankings = mainService.movieGenreRankings(ratings, movies);

        assertTrue(movieGenreRankings.containsKey("action"));
        assertTrue(movieGenreRankings.containsKey("drama"));
        assertTrue(movieGenreRankings.containsKey("war"));
        assertTrue(movieGenreRankings.containsKey("children's"));
        assertFalse(movieGenreRankings.containsKey("thriller"));
        assertEquals(0, Double.compare(5.0/3, movieGenreRankings.get("action")));
        assertEquals(0, Double.compare(4.0 + 2.0 + 5.0/3, movieGenreRankings.get("drama")));
        assertEquals(0, Double.compare(5.0/3, movieGenreRankings.get("war")));
        assertEquals(0, Double.compare(2, movieGenreRankings.get("children's")));
    }

    @Test
    public void testRecommendMovies () {
        HashMap<String, Double> movieGenreRankings = new HashMap<>();

        movieGenreRankings.put("action", 5.0/3);
        movieGenreRankings.put("drama", 4.0 + 2.0 + 5.0/3);
        movieGenreRankings.put("romance", 5.0/3);
        movieGenreRankings.put("children's", 2.0);

        List<String> moviesInfo = new ArrayList<>();

        moviesInfo.add("1::Toy Story (1995)::Animation|Children's|Comedy");
        moviesInfo.add("2::Jumanji (1995)::Adventure|Children's|Fantasy");
        moviesInfo.add("3::Grumpier Old Men (1995)::Comedy|Romance");
        moviesInfo.add("4::Waiting to Exhale (1995)::Comedy|Drama");
        moviesInfo.add("5::Father of the Bride Part II (1995)::Comedy");
        moviesInfo.add("6::Heat (1995)::Action|Crime|Thriller");
        moviesInfo.add("7::Sabrina (1995)::Comedy|Romance");
        moviesInfo.add("8::Tom and Huck (1995)::Adventure|Children's");
        moviesInfo.add("9::Sudden Death (1995)::Action");
        moviesInfo.add("10::GoldenEye (1995)::Action|Adventure|Thriller");
        moviesInfo.add("11::American President, The (1995)::Comedy|Drama|Romance");
        moviesInfo.add("13::Balto (1995)::Animation|Children's");
        moviesInfo.add("14::Nixon (1995)::Drama");
        moviesInfo.add("15::Cutthroat Island (1995)::Action|Adventure|Romance");
        moviesInfo.add("16::Casino (1995)::Drama|Thriller");
        moviesInfo.add("17::Sense and Sensibility (1995)::Drama|Romance");
        moviesInfo.add("20::Money Train (1995)::Action");

        List<MovieDetail> movies = new ArrayList<>();

        for (String movieInfo: moviesInfo) {
            String[] movieInfoSplit = movieInfo.split("::");
            String movieId = movieInfoSplit[0];
            String movieTitle = movieInfoSplit[1];
            List<String> movieGenres = Arrays.asList(movieInfoSplit[2].toLowerCase().split("\\|"));

            movies.add(new MovieDetail(movieId, movieTitle, movieGenres, "", ""));
        }

        List<MovieDetail> result = mainService.recommendMovies(movies, movieGenreRankings);

        assertEquals("14", result.get(0).movieId);
        assertEquals("8", result.get(1).movieId);
        assertEquals("9", result.get(2).movieId);
        assertEquals("17", result.get(3).movieId);
        assertEquals("5", result.get(4).movieId);
        assertEquals("4", result.get(5).movieId);
        assertEquals("13", result.get(6).movieId);
        assertEquals("20", result.get(7).movieId);
        assertEquals("11", result.get(8).movieId);
        assertEquals("16", result.get(9).movieId);
    }

    @Test
    public void testRecommendMoviesWithEmptyMoviesData () {
        HashMap<String, Double> movieGenreRankings = new HashMap<>();

        movieGenreRankings.put("action", 5.0/3);
        movieGenreRankings.put("drama", 4.0 + 2.0 + 5.0/3);
        movieGenreRankings.put("romance", 5.0/3);
        movieGenreRankings.put("children's", 2.0);

        List<MovieDetail> movies = new ArrayList<>();
        List<MovieDetail> result = mainService.recommendMovies(movies, movieGenreRankings);

        assertEquals(0, result.size());
    }

    @Test
    public void testRecommendMoviesByTitle() {
        ArrayList<String> moviesInfo = new ArrayList<>();
        String inputTitle1 = "sUper";
        String inputTitle2 = "Sabrina";
        int limit1 = 5;
        int limit2 = 10;

        moviesInfo.add("546::Super Mario Bros. (1993)::Action|Adventure|Children's|Sci-Fi");
        moviesInfo.add("651::Superweib, Das (1996)::Comedy"); // 4
        moviesInfo.add("3::Grumpier Old Men (1995)::Comedy|Romance"); // 3
        moviesInfo.add("4::Waiting to Exhale (1995)::Comedy|Drama"); // 2
        moviesInfo.add("5::Father of the Bride Part II (1995)::Comedy"); // 5
        moviesInfo.add("6::Heat (1995)::Action|Crime|Thriller");
        moviesInfo.add("7::Sabrina (1995)::Comedy|Romance"); // 4
        moviesInfo.add("8::Tom and Huck (1995)::Adventure|Children's");
        moviesInfo.add("9::Sudden Death (1995)::Action");
        moviesInfo.add("10::GoldenEye (1995)::Action|Adventure|Thriller");
        moviesInfo.add("25::Leaving Las Vegas (1995)::Drama|Romance"); // 3

        List<MovieDetail> movies = new ArrayList<>();

        for (String movieInfo: moviesInfo) {
            String[] movieInfoSplit = movieInfo.split("::");
            String movieId = movieInfoSplit[0];
            String movieTitle = movieInfoSplit[1];
            List<String> movieGenres = Arrays.asList(movieInfoSplit[2].toLowerCase().split("\\|"));

            movies.add(new MovieDetail(movieId, movieTitle, movieGenres, "", ""));
        }

        List<Rating> ratings = new ArrayList<>();

        ratings.add(new Rating("1", "546", 4.0));
        ratings.add(new Rating("1", "651", 4.0));
        ratings.add(new Rating("1", "3", 5.0));
        ratings.add(new Rating("1", "4", 2.0));
        ratings.add(new Rating("1", "5", 5.0));
        ratings.add(new Rating("1", "6", 3.0));
        ratings.add(new Rating("1", "7", 4.0));
        ratings.add(new Rating("1", "8", 4.0));
        ratings.add(new Rating("1", "9", 2.0));
        ratings.add(new Rating("1", "10", 5.0));
        ratings.add(new Rating("1", "10", 2.0));
        ratings.add(new Rating("1", "3", 1.0));
        ratings.add(new Rating("1", "25", 3.0));

        List<MovieDetail> result1 = mainService.recommendMoviesByTitle(inputTitle1, movies, limit1, ratings);
        List<MovieDetail> result2 = mainService.recommendMoviesByTitle(inputTitle1, movies, limit2, ratings);
        List<MovieDetail> result3 = mainService.recommendMoviesByTitle(inputTitle2, movies, limit2, ratings);

        assertEquals(5, result1.size());
        assertEquals("546", result1.get(0).movieId);
        assertEquals("8", result1.get(1).movieId);
        assertEquals("10", result1.get(2).movieId);
        assertEquals("6", result1.get(3).movieId);
        assertEquals("9", result1.get(4).movieId);

        assertEquals(5, result2.size());
        assertEquals("546", result2.get(0).movieId);
        assertEquals("8", result2.get(1).movieId);
        assertEquals("10", result2.get(2).movieId);
        assertEquals("6", result2.get(3).movieId);
        assertEquals("9", result2.get(4).movieId);

        assertEquals(6, result3.size());
        assertEquals("7", result3.get(0).movieId);
        assertEquals("5", result3.get(1).movieId);
        assertEquals("651", result3.get(2).movieId);
        assertEquals("3", result3.get(3).movieId);
        assertEquals("25", result3.get(4).movieId);
        assertEquals("4", result3.get(5).movieId);
    }

    @Test
    public void testRecommendMoviesByTitleWithMovieNotFound() {
        ArrayList<String> moviesInfo = new ArrayList<>();
        String inputTitle = "Crows and Sparrows";
        int limit = 10;

        moviesInfo.add("3::Grumpier Old Men (1995)::Comedy|Romance");
        moviesInfo.add("4::Waiting to Exhale (1995)::Comedy|Drama");
        moviesInfo.add("5::Father of the Bride Part II (1995)::Comedy");
        moviesInfo.add("6::Heat (1995)::Action|Crime|Thriller");
        moviesInfo.add("7::Sabrina (1995)::Comedy|Romance");

        List<MovieDetail> movies = new ArrayList<>();

        for (String movieInfo: moviesInfo) {
            String[] movieInfoSplit = movieInfo.split("::");
            String movieId = movieInfoSplit[0];
            String movieTitle = movieInfoSplit[1];
            List<String> movieGenres = Arrays.asList(movieInfoSplit[2].toLowerCase().split("\\|"));

            movies.add(new MovieDetail(movieId, movieTitle, movieGenres, "", ""));
        }

        List<Rating> ratings = new ArrayList<>();

        ratings.add(new Rating("1", "546", 4.0));
        ratings.add(new Rating("1", "651", 4.0));
        ratings.add(new Rating("1", "3", 5.0));
        ratings.add(new Rating("1", "4", 2.0));
        ratings.add(new Rating("1", "5", 5.0));
        ratings.add(new Rating("1", "6", 3.0));
        ratings.add(new Rating("1", "7", 4.0));
        ratings.add(new Rating("1", "8", 4.0));
        ratings.add(new Rating("1", "9", 2.0));
        ratings.add(new Rating("1", "10", 5.0));
        ratings.add(new Rating("1", "3", 2.0));

        List<MovieDetail> result = mainService.recommendMoviesByTitle(inputTitle, movies, limit, ratings);

        assertEquals(0, result.size());
    }
}
