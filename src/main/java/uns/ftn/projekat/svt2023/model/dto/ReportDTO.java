package uns.ftn.projekat.svt2023.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportDTO {
    @NotBlank
    private String reason;
    private String userUsername;
    private Integer postId;
    private Integer commentId;
}
