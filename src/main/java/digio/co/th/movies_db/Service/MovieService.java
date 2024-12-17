package digio.co.th.movies_db.Service;

import digio.co.th.movies_db.Dto.TMDB_Movie;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.GenresRepo;
import digio.co.th.movies_db.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepo movieRepo;

//    public List<Movies> getMovieAll(){
//        return movieRepo.findAll();
//    }

    public Page<Movies> getMovieAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepo.findAll(pageable);
    }

    public Movies getMovieById(Long id) {
        return movieRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public List<Movies> getMoviesByGenre(String genreName) {
        return movieRepo.findMoviesByGenres(genreName);
    }

    public Page<Movies> getMoviesAllGenre(String genreName, Pageable pageable) {
        return movieRepo.findMoviesAllGenres(genreName, pageable);
    }

    public void saveMovie(List<Movies> movies){
        movieRepo.saveAll(movies);
    }
}
