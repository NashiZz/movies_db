package digio.co.th.movies_db.Repository;

import digio.co.th.movies_db.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.username = :input OR u.email = :input")
    Optional<Users> findByUsernameOrEmail(@Param("input") String input);
}
