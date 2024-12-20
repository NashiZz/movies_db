package digio.co.th.movies_db.Controller;

import digio.co.th.movies_db.Dto.ReqSearchGenre;
import digio.co.th.movies_db.Dto.ReqSearchMovie;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.GenresRepo;
import digio.co.th.movies_db.Service.GenreService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static ch.qos.logback.core.util.StringUtil.isNullOrEmpty;

@RestController
@RequestMapping("api")
public class GenresController {

    private GenreService genreService;

    @Autowired
    private GenresRepo genresRepo;

    @Autowired
    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genres> getGenres() {
        return genreService.getGenresAll();
    }

    @GetMapping("/genres/search")
    public Page<Movies> searchGenres(@RequestParam String genre, Pageable pageable) {
        return genreService.searchMovieByGenres(genre, pageable);
    }

    @PostMapping("/genres/searchName")
    public Page<Genres> search(@RequestBody ReqSearchGenre req){
        return genresRepo.findAll((root, query, cb)->{
                    List<Predicate> predicates = new ArrayList<>();

                    if (!isNullOrEmpty(req.getName())){
                        String nameSearchPattern = "%" + req.getName() + "%";
                        predicates.add(cb.like(root.get("name"), nameSearchPattern));
                    }
                    return cb.and(predicates.toArray(new Predicate[0]));
                }
                ,req.getPageable()
        );
    }
}
