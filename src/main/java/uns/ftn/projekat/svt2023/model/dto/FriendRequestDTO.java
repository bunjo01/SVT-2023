package uns.ftn.projekat.svt2023.model.dto;

import lombok.*;
import uns.ftn.projekat.svt2023.model.entity.*;

@NoArgsConstructor
@Getter
@Setter
public class FriendRequestDTO {
    private Integer id;
    private Integer fromUserId;
    private Integer toUserId;

    public FriendRequestDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        this.fromUserId = friendRequest.getFromUser().getId();
        this.toUserId = friendRequest.getToUser().getId();
    }
}
