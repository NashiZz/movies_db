package digio.co.th.movies_db.Service;

import digio.co.th.movies_db.Entity.Users;
import digio.co.th.movies_db.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Register User
    public String registerUser(Users user) {
        if (usersRepo.existsByUsername(user.getUsername())) {
            return "Username already exists!";
        }

        if (usersRepo.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save user
        usersRepo.save(user);
        return "User registered successfully!";
    }

    public String loginUser(String usernameOrEmail, String password) {
        Optional<Users> user = usersRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (user.isEmpty()) {
            return "User not found!";
        }

        if (passwordEncoder.matches(password, user.get().getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid password!";
        }
    }
}
