package digio.co.th.movies_db.Controller;

import digio.co.th.movies_db.Dto.ReqSearchMovie;
import digio.co.th.movies_db.Dto.TMDBRes;
import digio.co.th.movies_db.Dto.TMDB_Movie;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.MovieRepo;
import digio.co.th.movies_db.Service.MovieService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static ch.qos.logback.core.util.StringUtil.isNullOrEmpty;
import static java.util.Objects.isNull;

@RestController
@RequestMapping("api")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepo movieRepo;

    @GetMapping("/fetch-movies")
    public String fetchMoviesFromTMDB() {
        String apiKey = "8d412ae1959630fbb306cdb1b45b0979";
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        TMDBRes response = restTemplate.getForObject(url, TMDBRes.class);

        if (response != null && response.getResults() != null) {
            List<Movies> movies = mapTMDBResultsToMovies(response.getResults());
            movieService.saveMovie(movies);
            return "Movies fetched and saved successfully!";
        } else {
            throw new RuntimeException("Failed to fetch movies from TMDB");
        }
    }

    private List<Movies> mapTMDBResultsToMovies(List<TMDB_Movie> results) {
        List<Movies> moviesList = new ArrayList<>();
        for (TMDB_Movie result : results) {
            Movies movie = new Movies();
            movie.setIdmovie(result.getId());
            movie.setTitle(result.getTitle());
            movie.setOverview(result.getOverview());
            movie.setRelease_date(result.getRelease_date());
            movie.setRating(result.getVote_average());
            movie.setPoster_path("https://image.tmdb.org/t/p/w500" + result.getPoster_path());
            moviesList.add(movie);
        }
        return moviesList;
    }

    @PostMapping("/movies/searchName")
    public Page<Movies> search(@RequestBody ReqSearchMovie req){
        return movieRepo.findAll((root, query, cb)->{
                    List<Predicate> predicates = new ArrayList<>();

                    if (!isNullOrEmpty(req.getTitle())){
                        String titleSearchPattern = "%" + req.getTitle() + "%";
                        predicates.add(cb.like(root.get("title"), titleSearchPattern));
                    }
                    return cb.and(predicates.toArray(new Predicate[0]));
                }
                ,req.getPageable()
        );
    }

    @GetMapping("/movies/{id}")
    public Movies getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

//    @GetMapping("/movies")
//    public List<Movies> getMovieAll() {
//        return movieService.getMovieAll();
//    }

    @GetMapping("/movies")
    public Page<Movies> getMovieAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return movieService.getMovieAll(page, size);
    }

    @GetMapping("/movies/genre")
    public List<Movies> getMoviesByGenre(@RequestParam String genre) {
        return movieService.getMoviesByGenre(genre);
    }
}

