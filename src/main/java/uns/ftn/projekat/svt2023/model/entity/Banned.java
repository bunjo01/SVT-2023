package uns.ftn.projekat.svt2023.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.*;

@Entity
@Table(name = "banned")
@NoArgsConstructor
@Getter
@Setter
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime timeStamp;
    @ManyToOne(fetch = FetchType.LAZY)
    private User bannedUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private User bannedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    private Group bannedFromGroup;

}
