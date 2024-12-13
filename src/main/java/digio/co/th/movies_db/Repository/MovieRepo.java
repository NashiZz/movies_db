package digio.co.th.movies_db.Repository;

import digio.co.th.movies_db.Entity.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepo extends JpaRepository<Movies,Long>, JpaSpecificationExecutor<Movies> {

    @Query("SELECT m FROM Movies m JOIN m.genres g WHERE  g.name = :genreName")
    List<Movies> findMoviesByGenres(@Param("genreName") String genreName);
}
