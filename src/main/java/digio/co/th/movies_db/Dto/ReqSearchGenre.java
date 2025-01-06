package digio.co.th.movies_db.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class ReqSearchGenre {
    @NotBlank(message = "Genre name must not be empty")
    private String name;

    @Min(value = 0, message = "Page number must be greater than or equal to 0")
    private Integer pageNo = 0;

    @Min(value = 1, message = "Page size must be greater than or equal to 1")
    private Integer pageSize = 10;

    public Pageable getPageable(){
        return PageRequest.of(pageNo, pageSize);
    }
}
