package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class UserProfileDTO {

    private Integer id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String description;

    public UserProfileDTO(User user) {
        this.firstName = user.getLastName();
        this.lastName = user.getLastName();
        this.description = user.getDescription();
    }

}
