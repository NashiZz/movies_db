package digio.co.th.movies_db.Controller;

import digio.co.th.movies_db.Entity.Users;
import digio.co.th.movies_db.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody Users user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Users user) {
        return userService.loginUser(user.getEmail(), user.getPassword());
    }
}
