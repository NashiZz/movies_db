package digio.co.th.movies_db.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TMDBRes {
    private List<TMDB_Movie> results;
}
