package digio.co.th.movies_db.Service;

import digio.co.th.movies_db.Dto.GenresRes;
import digio.co.th.movies_db.Dto.Genres_Movie;
import digio.co.th.movies_db.Dto.TMDBRes;
import digio.co.th.movies_db.Dto.TMDB_Movie;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.GenresRepo;
import digio.co.th.movies_db.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MovieDataLoader implements CommandLineRunner {

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private GenresRepo genresRepo;

    private final String TMDB_MOVIE_API_URL = "https://api.themoviedb.org/3/trending/all/day";
    private final String TMDB_GENRE_API_URL = "https://api.themoviedb.org/3/genre/movie/list";
    private final String API_KEY = "8d412ae1959630fbb306cdb1b45b0979";

    @Override
    public void run(String... args) throws Exception {
        loadGenres();
        loadMovies();
    }

//    private RestTemplate createRestTemplateWithTimeout() {
//        int connectTimeout = 5000;
//        int readTimeout = 5000;

    private void loadGenres() {
        String url = String.format("%s?api_key=%s&language=en-US", TMDB_GENRE_API_URL, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        GenresRes response = restTemplate.getForObject(url, GenresRes.class);

        if (response != null && response.getGenres() != null) {
            for (Genres_Movie genresMovie : response.getGenres()) {
                Genres genres = new Genres();
                genres.setIdgen(genresMovie.getId());
                genres.setName(genresMovie.getName());

                if (!genresRepo.existsById(genres.getIdgen())) {
                    genresRepo.save(genres);
                }
            }
        }
    }

    private void loadMovies() {
        String url = String.format("%s?api_key=%s&language=en-US", TMDB_MOVIE_API_URL, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        TMDBRes movieResponse = restTemplate.getForObject(url, TMDBRes.class);

        if (movieResponse != null && movieResponse.getResults() != null) {
            for (TMDB_Movie tmdbMovie : movieResponse.getResults()) {
                Optional<Movies> existingMovie = movieRepo.findById(tmdbMovie.getId());

                Movies movie = existingMovie.orElseGet(Movies::new);
                movie.setIdmovie(tmdbMovie.getId());

                if (tmdbMovie.getTitle() != null && !tmdbMovie.getTitle().isEmpty()) {
                    movie.setTitle(tmdbMovie.getTitle());
                } else if (tmdbMovie.getName() != null && !tmdbMovie.getName().isEmpty()) {
                    movie.setTitle(tmdbMovie.getName());
                }

                movie.setOverview(tmdbMovie.getOverview());

                if (tmdbMovie.getRelease_date() != null && !tmdbMovie.getRelease_date().isEmpty()) {
                    movie.setRelease_date(tmdbMovie.getRelease_date());
                } else if (tmdbMovie.getFirst_air_date() != null && !tmdbMovie.getFirst_air_date().isEmpty()) {
                    movie.setRelease_date(tmdbMovie.getFirst_air_date());
                }

                movie.setRating(tmdbMovie.getVote_average());
                movie.setPoster_path(tmdbMovie.getPoster_path());
                movie.setBackground_path(tmdbMovie.getBackdrop_path());

                Set<Genres> movieGenres = new HashSet<>(genresRepo.findAllById(tmdbMovie.getGenre_ids()));

                if (!movieGenres.isEmpty()) {
                    movie.setGenres(movieGenres);

                    movieRepo.save(movie);
                } else {
                    System.out.println("No genres found for movie with ID: " + tmdbMovie.getId());
                }
            }
        }
    }
}
