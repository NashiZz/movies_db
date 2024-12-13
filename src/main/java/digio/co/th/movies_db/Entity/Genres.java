package digio.co.th.movies_db.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genres {

    @Id
    private Long idgen;

    @Column(length = 50)
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "genres")
    private Set<Movies> movies;
}
