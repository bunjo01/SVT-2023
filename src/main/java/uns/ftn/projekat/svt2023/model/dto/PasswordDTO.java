package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import org.springframework.context.annotation.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String current;
    @NotBlank
    private String password;
    @NotBlank
    private String confirm;

}
