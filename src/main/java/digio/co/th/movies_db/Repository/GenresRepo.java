package digio.co.th.movies_db.Repository;

import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenresRepo extends JpaRepository<Genres,Long> , JpaSpecificationExecutor<Genres> {

    @Query("SELECT m FROM Movies m JOIN m.genres g WHERE g.name LIKE %:genre%")
    Page<Movies> findMovieByGenre(@Param("genre") String genre, Pageable pageable);
}
