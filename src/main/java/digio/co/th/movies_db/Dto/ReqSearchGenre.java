package digio.co.th.movies_db.Dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class ReqSearchGenre {
    private String genre;

    private Integer pageNo = 0;
    private Integer pageSize = 10;

    public Pageable getPageable(){
        return PageRequest.of(pageNo, pageSize);
    }
}