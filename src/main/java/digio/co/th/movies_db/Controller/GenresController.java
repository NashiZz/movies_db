package digio.co.th.movies_db.Controller;

import digio.co.th.movies_db.Dto.ReqSearchGenre;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class GenresController {

    private GenreService genreService;

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
}
