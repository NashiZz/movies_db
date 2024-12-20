package digio.co.th.movies_db.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long uid;

    @Column(length = 255)
    private String username;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String lastname;

    @Column(length = 500)
    private String address;

    @Column(length = 50)
    private String email;

    @Column(length = 255)
    private String password;
}
