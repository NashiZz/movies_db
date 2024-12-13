package digio.co.th.movies_db.Service;

import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.GenresRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private GenresRepo genresRepo;

    @Autowired
    public GenreService(GenresRepo genresRepo) {
        this.genresRepo = genresRepo;
    }

    public List<Genres> getGenresAll(){
        return genresRepo.findAll();
    }

    public Page<Movies> searchMovieByGenres(String genre, Pageable pageable) {
        return genresRepo.findMovieByGenre(genre, pageable);
    }


}
