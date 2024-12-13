package digio.co.th.movies_db.Dto;

import lombok.Data;

import java.util.List;

@Data
public class GenresRes {
    private List<Genres_Movie> genres;
}
