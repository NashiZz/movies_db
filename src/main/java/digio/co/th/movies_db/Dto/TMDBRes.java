package digio.co.th.movies_db.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TMDBRes {
    private Dates dates;
    private Integer page;
    private List<TMDB_Movie> results;
    private Integer total_pages;
    private Integer total_results;

    @Data
    public static class Dates {
        private String maximum;
        private String minimum;
    }
}
