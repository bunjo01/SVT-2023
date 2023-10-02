package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private Integer id;
    @NotBlank
    private String content;
    private String creationDate;
    private String userUsername;
    private String imagePath;

    public PostDTO(Post createdPost) {
        this.id = createdPost.getId();
        this.content = createdPost.getContent();
        this.creationDate = createdPost.getCreationDate().toString();
        this.userUsername = createdPost.getUser().getUsername();
    }

}
