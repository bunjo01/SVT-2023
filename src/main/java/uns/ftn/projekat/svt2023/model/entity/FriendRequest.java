package uns.ftn.projekat.svt2023.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "friendRequest")
@NoArgsConstructor
@Getter
@Setter
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Boolean approved;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime at;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;

    public FriendRequest(Integer id, Boolean approved, LocalDateTime createdAt, LocalDateTime at) {
        this.id = id;
        this.approved = approved;
        this.createdAt = createdAt;
        this.at = at;
    }

}
