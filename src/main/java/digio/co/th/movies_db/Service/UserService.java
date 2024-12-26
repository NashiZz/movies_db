package digio.co.th.movies_db.Service;


import com.google.firebase.remoteconfig.internal.TemplateResponse;
import digio.co.th.movies_db.Config.JwtUtil;

import digio.co.th.movies_db.Dto.UserReq;
import digio.co.th.movies_db.Dto.UserRes;
import digio.co.th.movies_db.Entity.Users;
import digio.co.th.movies_db.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UsersRepo usersRepo, JwtUtil jwtUtil) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public List<Users> getUsersAll(){
        return usersRepo.findAll();
    }

    public Optional<Users> getUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<Users> user = usersRepo.findByUsernameOrEmail(usernameOrEmail);
        return user;
    }

    // Register User
    public String registerUser(UserReq userReq) {
        if (usersRepo.existsByUsername(userReq.getUsername())) {
            return "Username already exists!";
        }

        if (usersRepo.existsByEmail(userReq.getEmail())) {
            return "Email already registered!";
        }

        Users user = new Users();
        user.setUsername(userReq.getUsername());
        user.setName(userReq.getFirstname());
        user.setLastname(userReq.getLastname());
        user.setAddress(userReq.getAddress());
        user.setEmail(userReq.getEmail());

        String hashedPassword = passwordEncoder.encode(userReq.getPassword());
        user.setPassword(hashedPassword);

        user.setImg_profile(userReq.getImg_profile());

        usersRepo.save(user);
        return "User registered successfully!";
    }

    public ResponseEntity<?> loginUser(String usernameOrEmail, String password) {
        Optional<Users> userOpt = usersRepo.findByUsernameOrEmail(usernameOrEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        Users user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Generate token
            String token = jwtUtil.generateToken(user.getUsername());

            // Return token and user info
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", new UserRes(user.getUid(), user.getUsername()));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password!");
        }
    }
}
