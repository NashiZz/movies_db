package digio.co.th.movies_db.Dto;

import lombok.Data;
@Data
public class UserReq {
    private String username;
    private String firstname;
    private String lastname;
    private String address;
    private String email;
    private String password;
    private String img_profile;
}
