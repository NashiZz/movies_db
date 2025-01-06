package digio.co.th.movies_db.Service;

import digio.co.th.movies_db.Dto.GenresRes;
import digio.co.th.movies_db.Dto.Genres_Movie;
import digio.co.th.movies_db.Dto.TMDBRes;
import digio.co.th.movies_db.Dto.TMDB_Movie;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import digio.co.th.movies_db.Repository.GenresRepo;
import digio.co.th.movies_db.Repository.MovieRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MovieDataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MovieDataLoader.class);

    @Value("${tmdb.api.key}")
    private String API_KEY;

    private MovieRepo movieRepo;
    private GenresRepo genresRepo;
    private RestTemplate restTemplate;

    @Autowired
    public MovieDataLoader(MovieRepo movieRepo, GenresRepo genresRepo, RestTemplate restTemplate) {
        this.movieRepo = movieRepo;
        this.genresRepo = genresRepo;
        this.restTemplate = restTemplate;
    }

    private final String TMDB_MOVIE_API_URL = "https://api.themoviedb.org/3/movie/now_playing";
    private final String TMDB_GENRE_API_URL = "https://api.themoviedb.org/3/genre/movie/list";

    @Override
    public void run(String... args) throws Exception {
        loadGenres();
        loadMovies();
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void loadMoviesDaily() {
        loadMovies();
    }

    private void loadGenres() {
        try {
            String url = String.format("%s?api_key=%s&language=en-US", TMDB_GENRE_API_URL, API_KEY);
            GenresRes response = restTemplate.getForObject(url, GenresRes.class);

            if (response != null && response.getGenres() != null) {
                for (Genres_Movie genresMovie : response.getGenres()) {
                    if (!genresRepo.existsById(genresMovie.getId())) {
                        Genres genres = new Genres();
                        genres.setIdgen(genresMovie.getId());
                        genres.setName(genresMovie.getName());
                        genresRepo.save(genres);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while loading genres", e);
        }
    }

    private void loadMovies() {
        int page = 1;
        int totalPages = 1;
        int maxPages = 5;

        while (page <= totalPages && page <= maxPages) {
            try {
                String url = String.format("%s?api_key=%s&language=en-US&page=%d", TMDB_MOVIE_API_URL, API_KEY, page);
                TMDBRes movieResponse = restTemplate.getForObject(url, TMDBRes.class);

                if (movieResponse != null && movieResponse.getResults() != null) {
                    totalPages = movieResponse.getTotal_pages();
                    processMovies(movieResponse.getResults());
                }

                page++;

            } catch (Exception e) {
                logger.error("Error occurred while loading movies for page: {}", page, e);
                break;
            }
        }
    }

    @Transactional
    private void processMovies(List<TMDB_Movie> tmdbMovies) {
        for (TMDB_Movie tmdbMovie : tmdbMovies) {
            Movies movie = movieRepo.findById(tmdbMovie.getId()).orElseGet(Movies::new);
            movie.setIdmovie(tmdbMovie.getId());
            setMovieDetails(movie, tmdbMovie);

            Set<Genres> movieGenres = new HashSet<>(genresRepo.findAllById(tmdbMovie.getGenre_ids()));
            if (!movieGenres.isEmpty()) {
                movie.setGenres(movieGenres);
                movieRepo.save(movie);
            } else {
                System.out.println("No genres found for movie with ID: " + tmdbMovie.getId());
            }
        }
    }

    private void setMovieDetails(Movies movie, TMDB_Movie tmdbMovie) {
        movie.setTitle(StringUtils.defaultIfBlank(tmdbMovie.getTitle(), tmdbMovie.getName()));
        movie.setOverview(tmdbMovie.getOverview());
        movie.setRelease_date(StringUtils.defaultIfBlank(tmdbMovie.getRelease_date(), tmdbMovie.getFirst_air_date()));
        movie.setRating(tmdbMovie.getVote_average());
        movie.setPoster_path(tmdbMovie.getPoster_path());
        movie.setBackground_path(tmdbMovie.getBackdrop_path());
    }
}
