package mymovie.services;

import mymovie.model.*;
import mymovie.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.parseInt;

@EnableMongoRepositories(basePackages = "com.applesauce.repository")
@Service
public class MainService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImdbRepository imdbRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    public static class IncorrectInputException extends Exception {
        public IncorrectInputException(String message) {
            super(message);
        }
    }

    public static final String[] ACCEPTABLE_MOVIE_GENRES = {
            "Action", "Adventure", "Animation",
            "Children's", "Comedy", "Crime",
            "Documentary", "Drama", "Fantasy",
            "Film-Noir", "Horror", "Musical",
            "Mystery", "Romance", "Sci-Fi",
            "Thriller", "War", "Western"
    };

    private static final String[] OCCUPATIONS = {
            "other", "academic/educator", "artist",
            "clerical/admin", "college/grad student",
            "customer service", "doctor/health care",
            "executive/managerial", "farmer", "homemaker",
            "K-12 student", "lawyer", "programmer",
            "retired", "sales/marketing", "scientist",
            "self-employed", "technician/engineer", "tradesman/craftsman",
            "unemployed", "writer"
    };

    public void validateInput (String[] args) throws IncorrectInputException {
        final int argsLength = args.length;
        final int MIN_AGE = 1;
        final int MAX_AGE = 123;

        try {
            if (argsLength != 3 && argsLength != 4) {
                throw new IncorrectInputException("Incorrect argc");
            } else {
                for (String arg: args) {
                    if (arg == null) {
                        throw new IncorrectInputException("One of the args is not specified");
                    }
                }

                final String gender = args[0].toUpperCase();
                final String argsAge = args[1];
                final int age = !argsAge.isEmpty() ? parseInt(args[1]) : MIN_AGE;

                if (!gender.equals("F") && !gender.isEmpty() && !gender.equals("M")) {
                    throw new IncorrectInputException("Incorrect gender format");
                }

                if (age < MIN_AGE || age > MAX_AGE) {
                    throw new IncorrectInputException("Incorrect age format");
                }

                if (argsLength == 4) {
                    splitMovieGenres(args[3]);
                }
            }
        } catch (IncorrectInputException exception) {
            throw exception;
        } catch (NumberFormatException exception) {
            throw new IncorrectInputException("Incorrect age format");
        }
    }

    public List<Rating> getRatingsFromUsers(List<User> users){
        List<String> userIds = new ArrayList<String>();

        for(User user: users) {
            userIds.add(user.userId);
        }

        return ratingRepository.sampleRatingsByUserId(userIds.toArray(new String[0]), 100000);
    }

    public ArrayList<Integer> similarOccupationsList(String occupation){
        Integer[][] similarOccupationsIDsList = new Integer[][]{
                {13, 19}, {1, 4}, {3, 7}, {12, 15}, {0}, {2}, {5},
                {6}, {8}, {9}, {10}, {11}, {14}, {16}, {17}, {18}, {20}
        };
        ArrayList<Integer> ret = new ArrayList<Integer>();

        if(occupation.equals("")){
            for(int i = 0; i <= 20; i++){
                ret.add(i);
            }
        }else {
            int occupationID = getOccupationId(occupation);

            for (Integer[] currentGroup : similarOccupationsIDsList) {
                ArrayList<Integer> currentGroupAsList = new ArrayList<Integer>();

                for (int id : currentGroup) {
                    currentGroupAsList.add(id);
                }
                if (currentGroupAsList.contains(occupationID)) {
                    ret = currentGroupAsList;
                    break;
                }
            }
        }

        return ret;
    }

    public List<User> getUsersFromGivenConstraints(String gender, String age, List<Integer> occupationIDs){
        int ageCategory = 0;

        if(!age.equals("")) {
            if (parseInt(age) < 18) {
                ageCategory = 1;
            } else if (parseInt(age) <= 24) {
                ageCategory = 18;
            } else if (parseInt(age) <= 34) {
                ageCategory = 25;
            } else if (parseInt(age) <= 44) {
                ageCategory = 35;
            } else if (parseInt(age) <= 49) {
                ageCategory = 45;
            } else if (parseInt(age) <= 55) {
                ageCategory = 50;
            } else {
                ageCategory = 56;
            }
        }

        List<User> users = userRepository.findAll();
        List<User> filteredUsers = new ArrayList<>();

        for (User user: users) {
            if((gender.equals("") || user.gender.equals(gender.toUpperCase()))
                && (age.equals("") || user.age == ageCategory)
                && occupationIDs.contains(user.occupation)
            ) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

    public static int getOccupationId(String occupation) {
        for (int i = 0; i < OCCUPATIONS.length; ++i){
            final String[] splitOccupations = OCCUPATIONS[i].split("\\/");

            for (int j = 0; j < splitOccupations.length; ++j){
                if (splitOccupations[j].toLowerCase().equals(occupation)) {
                    return i;
                }
            }
        }

        return 0;
    }

    public String[] splitMovieGenres(String movieGenres) throws IncorrectInputException {
        if (movieGenres.equals("")){
            return new String[0];
        }

        String lowercaseMovieGenres = movieGenres.toLowerCase();
        String[] splitMovieGenres = lowercaseMovieGenres.split(",", 0);

        for(int i = 0; i < splitMovieGenres.length; ++i){
            final String testedGenre = splitMovieGenres[i];
            boolean rejected = true;

            for(int j = 0; j < MainService.ACCEPTABLE_MOVIE_GENRES.length; ++j){
                if (testedGenre.equals(MainService.ACCEPTABLE_MOVIE_GENRES[j].toLowerCase())) {
                    rejected = false;
                }
            }

            if (rejected) {
                throw new IncorrectInputException("Incorrect movie genres format");
            }
        }

        return splitMovieGenres;
    }

    public HashMap<String, Double> movieGenreRankings (List<Rating> ratings, List<MovieDetail> movies) {
        HashMap<String, List<String>> movieIdToGenres = new HashMap<String, List<String>>();
        HashMap<String, Double> movieGenreRankings = new HashMap<String, Double>();

        for (MovieDetail movie: movies) {
            movieIdToGenres.put(movie.movieId, movie.genres);
        }

        for (Rating rating: ratings) {
            if (!movieIdToGenres.containsKey(rating.movieId)) {
                continue;
            }

            final List<String> movieGenres = movieIdToGenres.get(rating.movieId);

            for (String genre: movieGenres) {
                final double calculatedRanking = rating.rating/movieGenres.size();

                if (movieGenreRankings.containsKey(genre)) {
                    final double currentRanking = movieGenreRankings.get(genre);

                    movieGenreRankings.replace(genre, calculatedRanking + currentRanking);
                } else {
                    movieGenreRankings.put(genre, calculatedRanking);
                }
            }
        }

        return movieGenreRankings;
    }

    public List<MovieDetail> filteredListOfMoviesWithIMDB(String[] genres) {
        HashMap<String, String> movieIdToImdb = new HashMap<String, String>();
        HashMap<String, String> movieIdToThumbnail = new HashMap<String, String>();

        List<Imdb> imdbIds = imdbRepository.findAll();
        List<Thumbnail> thumbnails = thumbnailRepository.findAll();

        for (Imdb imdbId: imdbIds) {
            movieIdToImdb.put(imdbId.movieId, imdbId.imdb);
        }

        for (Thumbnail thumbnail: thumbnails) {
            movieIdToThumbnail.put(thumbnail.movieId, thumbnail.thumbnail);
        }

        List<Movie> movies = genres.length == 0 ? movieRepository.findAll() : movieRepository.findMoviesByGenres(genres);
        List<MovieDetail> moviesDetail = new ArrayList<>();

        for (Movie movie: movies) {
            moviesDetail.add(new MovieDetail(
                    movie.movieId,
                    movie.title,
                    movie.genres,
                    "http://www.imdb.com/title/tt" + movieIdToImdb.getOrDefault(movie.movieId, ""),
                    movieIdToThumbnail.getOrDefault(movie.movieId, "")
            ));
        }

        return moviesDetail;
    }

    public List<MovieDetail> recommendMoviesByTitle(String inputMovieTitle, List<MovieDetail> movies, int limit, List<Rating> ratings) {
        List<MovieDetail> recommendedMovies = new ArrayList<>();
        MovieDetail matchedMovieByTitle = null;

        for (MovieDetail movie : movies) {
            if (movie.title.toLowerCase().contains(inputMovieTitle.toLowerCase())) {
                matchedMovieByTitle = movie;
                break;
            }
        }

        if (matchedMovieByTitle == null){
            return recommendedMovies;
        }

        List<MovieDetail> similarMoviesByGenre = new ArrayList<>();

        for (MovieDetail movie : movies){
            for (String genre : movie.genres){
                if (movie.movieId != matchedMovieByTitle.movieId && matchedMovieByTitle.genres.contains(genre)) {
                    similarMoviesByGenre.add(movie);
                    break;
                }
            }
        }

        HashMap<String, Double> movieAverageRatings = getMovieAverageRatings(similarMoviesByGenre, ratings);

        Collections.sort(similarMoviesByGenre, new Comparator<MovieDetail>() {
            @Override
            public int compare(MovieDetail movie1, MovieDetail movie2) {
                Double movie1Rating = movieAverageRatings.get(movie1.movieId);
                Double movie2Rating = movieAverageRatings.get(movie2.movieId);

                if (movie1Rating > movie2Rating) {
                    return -1;
                } else if (movie1Rating < movie2Rating) {
                    return 1;
                }

                return 0;
            }
        });

        recommendedMovies.add(matchedMovieByTitle);
        recommendedMovies.addAll(similarMoviesByGenre);
        recommendedMovies = recommendedMovies.subList(0, Math.min(Math.max(0, limit), recommendedMovies.size()));

        return recommendedMovies;
    }

    public List<MovieDetail> recommendMovies (List<MovieDetail> movies, HashMap<String, Double> movieGenreRankings) {
        List<MovieDetail> recommendedMovies = new ArrayList<>();
        TreeMap<String, List<MovieDetail>> movieGenreToMoviesOfGenre = new TreeMap<>();
        movieGenreToMoviesOfGenre.put("none", new ArrayList<>());

        for (MovieDetail movie: movies) {
            boolean belongsToGenreRankings = false;

            for (String genre: movieGenreRankings.keySet()) {
                if (movie.genres.contains(genre)) {
                    belongsToGenreRankings = true;

                    if (!movieGenreToMoviesOfGenre.containsKey(genre)) {
                        movieGenreToMoviesOfGenre.put(genre, new ArrayList<>());
                    }

                    movieGenreToMoviesOfGenre.get(genre).add(movie);
                }
            }

            if (!belongsToGenreRankings) {
                movieGenreToMoviesOfGenre.get("none").add(movie);
            }
        }

        for (Map.Entry<String, List<MovieDetail>> entry: movieGenreToMoviesOfGenre.entrySet()) {
            Collections.sort(entry.getValue(), new Comparator<MovieDetail>() {
                @Override
                public int compare(MovieDetail movie1, MovieDetail movie2) {
                    double movie1Ranking = 0;
                    double movie2Ranking = 0;

                    for (String movie1Genre: movie1.genres) {
                        if (movieGenreRankings.containsKey(movie1Genre)) {
                            movie1Ranking += movieGenreRankings.get(movie1Genre)/movie1.genres.size();
                        }
                    }

                    for (String movie2Genre: movie2.genres) {
                        if (movieGenreRankings.containsKey(movie2Genre)) {
                            movie2Ranking += movieGenreRankings.get(movie2Genre)/movie2.genres.size();
                        }
                    }

                    if (movie1Ranking > movie2Ranking) {
                        return -1;
                    } else if (movie1Ranking < movie2Ranking) {
                        return 1;
                    }

                    return 0;
                }
            });
        }

        movieGenreRankings.put("none", 0.0);

        ArrayList<String> movieGenres = new ArrayList<>(movieGenreToMoviesOfGenre.keySet());

        Collections.sort(movieGenres, new Comparator<String>() {
            @Override
            public int compare(String genre1, String genre2) {
                if (movieGenreRankings.get(genre1) > movieGenreRankings.get(genre2)) {
                    return -1;
                } else if (movieGenreRankings.get(genre1) < movieGenreRankings.get(genre2)) {
                    return 1;
                }

                return 0;
            }
        });

        while (recommendedMovies.size() <= 10 && movieGenreToMoviesOfGenre.size() > 0) {
            for (String genre: movieGenres) {
                if (movieGenreToMoviesOfGenre.containsKey(genre)) {
                    List<MovieDetail> moviesOfGenre = movieGenreToMoviesOfGenre.get(genre);

                    while (moviesOfGenre.size() > 0 && recommendedMovies.contains(moviesOfGenre.get(0))) {
                        moviesOfGenre.remove(0);
                    }

                    if (moviesOfGenre.size() == 0) {
                        movieGenreToMoviesOfGenre.remove(genre);
                    } else {
                        recommendedMovies.add(moviesOfGenre.get(0));

                        moviesOfGenre.remove(0);
                    }
                }
            }
        }

        if (recommendedMovies.size() > 10) {
            recommendedMovies = new ArrayList<>(recommendedMovies.subList(0, 10));
        }

        return recommendedMovies;
    }

    public HashMap<String, Double> getMovieAverageRatings (List<MovieDetail> movies, List<Rating> ratings) {
        HashMap<String, Double> averageRating = new HashMap<String, Double>();
        HashMap<String, Double> totalRatingsSum = new HashMap<String, Double>();
        HashMap<String, Integer> numberOfUsersForMovie = new HashMap<String, Integer>();

        for(Rating rating: ratings) {
            totalRatingsSum.put(rating.movieId, totalRatingsSum.getOrDefault(rating.movieId, 0.0) + rating.rating);
            numberOfUsersForMovie.put(rating.movieId, numberOfUsersForMovie.getOrDefault(rating.movieId, 0) + 1);
        }

        for (MovieDetail movie : movies) {
            averageRating.put(movie.movieId, Double.valueOf(totalRatingsSum.getOrDefault(movie.movieId, 0.0)) / Double.valueOf(numberOfUsersForMovie.getOrDefault(movie.movieId, 1)));
        }

        return averageRating;
    }
}