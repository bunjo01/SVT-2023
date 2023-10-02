package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.validation.constraints.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDTO {

    private Integer id;
    @NotBlank
    private String userUsername;
    @NotBlank
    private Integer groupdId;
    private LocalDateTime created;
    private boolean approved;

    public GroupRequestDTO(GroupRequest groupRequest) {
        this.id = groupRequest.getId();
        this.userUsername = groupRequest.getUser().getUsername();
        this.groupdId = groupRequest.getGroup().getId();
        this.created = groupRequest.getCreated();
    }
}
