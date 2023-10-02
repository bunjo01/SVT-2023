package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuspendDTO {

    @NotBlank
    private String description;
    private String groupName;

    public SuspendDTO(Group group) {
        this.description = group.getSuspendedReason();
        this.groupName = group.getName();
    }

}
