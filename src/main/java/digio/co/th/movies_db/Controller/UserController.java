package digio.co.th.movies_db.Controller;

import digio.co.th.movies_db.Dto.LoginReq;
import digio.co.th.movies_db.Dto.UserReq;
import digio.co.th.movies_db.Entity.Genres;
import digio.co.th.movies_db.Entity.Users;
import digio.co.th.movies_db.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<Users> getGenres() {
        return userService.getUsersAll();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserReq userReq) {
        String response = userService.registerUser(userReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        return userService.loginUser(loginReq.getUsernameOrEmail(), loginReq.getPassword());
    }

    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfile(@RequestParam String usernameOrEmail) {
        Optional<Users> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
