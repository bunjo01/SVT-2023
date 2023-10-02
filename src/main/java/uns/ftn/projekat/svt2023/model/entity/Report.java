package uns.ftn.projekat.svt2023.model.entity;

import lombok.*;
import uns.ftn.projekat.svt2023.model.enums.*;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "report")
@NoArgsConstructor
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private ReportReason reason;
    @Column
    private LocalDateTime timeStamp;
    @Column
    private Boolean accepted;
    @ManyToOne
    @JoinColumn(name = "byUser_id")
    private User byUser;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportedUser;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment reportedComment;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post reportedPost;


    public Report(Integer id, ReportReason reason, LocalDateTime timeStamp, Boolean accepted) {
        this.id = id;
        this.reason = reason;
        this.timeStamp = timeStamp;
        this.accepted = accepted;
    }
}
