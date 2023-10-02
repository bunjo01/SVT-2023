package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.validation.constraints.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
public class ReactionDTO {
    private Integer id;
    @NotBlank
    private String reactionType;
    @NotBlank
    private String userUsername;
    private String postContent;
    private LocalDateTime timeStamp;

    public ReactionDTO(Reaction reaction) {
        this.id = reaction.getId();
        this.userUsername = reaction.getUser().getUsername();
        this.reactionType = reaction.getType().toString();
        this.postContent = reaction.getPost().getContent();
        this.timeStamp = reaction.getTimeStamp();
    }
}
