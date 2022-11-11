package com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.controller;


import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.exceptionHandling.MovieException;
import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.model.Movie;
import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.service.MovieService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping(value = "/add-Movie" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie savingAMovie(@RequestBody Movie movie) throws MovieException {
        if(movie.getName()==null){
            throw new MovieException("Movie name must not be null.");
        }
        if(movie.getReleaseDate()==null){
            throw new MovieException("Release Date must not be null.");
        }
        return movieService.saveMovie(movie);
    }

    @GetMapping("/getAllMovies")
    public List<Movie> GatMovies() {
        return  movieService.getMovies();
    }

    @DeleteMapping("/deleteMovie/{id}")
    public String deleteMovie(@PathVariable("id") ObjectId id) throws MovieException {
        try
        {
            movieService.getMovieById(id);
            return  movieService.deleteMovies(id);
        }
        catch (Exception e) {
            throw new MovieException("MOVIE DOESN't EXIST WITH THIS ID");
        }
    }

    @GetMapping("/getMovieById/{id}")
    public Movie getMovie(@PathVariable("id") ObjectId id) throws MovieException {
        Movie movie = null;
        try
        {
            movie = movieService.getMovieById(id);
            return movie;
        }
        catch (Exception e){
            if(movie==null){
                throw new MovieException("MOVIE DOESN't EXIST WITH THIS ID");
            }
        }
        throw new MovieException("Unknown Error.");

    }

    @PatchMapping(value = "/updateMovie" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie updateMovie(@RequestBody Movie movie) throws MovieException {
        Movie movie_ = null;
        try
        {
            movie_ = movieService.getMovieById(movie.getId());
            return movieService.updateMovies(movie);
        }
        catch (Exception e){
            if(movie==null){
                throw new MovieException("MOVIE DOESN't EXIST WITH THIS ID");
            }
            throw new MovieException(e.getMessage());
        }
    }


}
