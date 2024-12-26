package digio.co.th.movies_db.Dto;

import lombok.Data;

@Data
public class UserRes {
    private Long uid;
    private String username;

    public UserRes(Long uid, String username) {
        this.uid = uid;
        this.username = username;
    }
}
