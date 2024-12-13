package digio.co.th.movies_db.Dto;

import lombok.Data;

@Data
public class Genres_Movie {
    private Long id;
    private String name;

    public Genres_Movie(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
