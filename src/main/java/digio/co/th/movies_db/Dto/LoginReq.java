package digio.co.th.movies_db.Dto;

import lombok.Data;

@Data
public class LoginReq {
    private String usernameOrEmail;
    private String password;
}
