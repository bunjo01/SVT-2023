package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;

@NoArgsConstructor
@Getter
@Setter
public class GroupDTO {

    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String creationDate;
    private Boolean isSuspended;
    private String suspendedReason;

    public GroupDTO(Group createdGroup) {
        this.id = createdGroup.getId();
        this.name = createdGroup.getName();
        this.description = createdGroup.getDescription();
        this.creationDate = createdGroup.getCreationDate().toString();
        this.isSuspended = createdGroup.getSuspended();
        this.suspendedReason = createdGroup.getSuspendedReason();
    }

}
