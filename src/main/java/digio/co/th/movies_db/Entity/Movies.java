package digio.co.th.movies_db.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
public class Movies {

    @Id
    private Long idmovie;

    @Column(length = 255)
    private String title;

    @Column(length = 2500)
    private String overview;

    private String release_date;

    @Column(length = 255)
    private String poster_path;

    @Column(length = 255)
    private String background_path;

    private Double rating;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "idmovie"),
            inverseJoinColumns = @JoinColumn(name = "idgen")
    )
    private Set<Genres> genres;
}
