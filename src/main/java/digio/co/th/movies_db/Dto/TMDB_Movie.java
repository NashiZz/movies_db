package digio.co.th.movies_db.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TMDB_Movie {
    private Long id;
    private String title;
    private String name;
    private String overview;
    private String release_date;
    private String first_air_date;
    private Double vote_average;
    private String poster_path;
    private String backdrop_path;
    private List<Long> genre_ids;
}