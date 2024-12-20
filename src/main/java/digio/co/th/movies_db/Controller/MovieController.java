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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/movies")
    public Page<Movies> getMovieAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return movieService.getMovieAll(page, size);
    }

    @GetMapping("/movies/Allgenre")
    public Page<Movies> getMoviesByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return movieService.getMoviesAllGenre(genre, pageable);
    }

    @GetMapping("/movies/genre")
    public List<Movies> getMoviesByGenre(@RequestParam String genre) {
        return movieService.getMoviesByGenre(genre);
    }
}

