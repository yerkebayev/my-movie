package mymovie.controllers;

import mymovie.services.MainService;

public class UsersErrorResponse {
    public int statusCode;
    public String message;

    public UsersErrorResponse (int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = "Reason: " + message + ". " +
            "Expected parameters: " +
            "gender (F or M), age (number), occupation, genres. " +
            "Each parameter can be omitted. " +
            "Example:  " +
            "?gender=F&occupation=academic, " +
            "?gender=F&occupation=academic&genres=Animation,Drama,Fantasy " +
            "List of supported movie categories: " +
            String.join("|", MainService.ACCEPTABLE_MOVIE_GENRES) + ". " +
            "You can enter list of categories delimiting them with ,, however "
            + "there should be no spaces in between and categories should fall into the list of " +
            "acceptable categories. Inputs are case insensitive";
    }
}
