package uns.ftn.projekat.svt2023.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uns.ftn.projekat.svt2023.model.entity.Post;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Integer id;
    @NotBlank
    private String content;
    @NotBlank
    private String title;
    private String creationDate;
    private String userUsername;
    private String imagePath;

    public PostDTO(Post createdPost) {
        this.id = createdPost.getId();
        this.content = createdPost.getContent();
        this.creationDate = createdPost.getCreationDate().toString();
        this.userUsername = createdPost.getUser().getUsername();
        this.title = createdPost.getTitle();
    }
}
