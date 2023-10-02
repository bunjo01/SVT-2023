package uns.ftn.projekat.svt2023.model.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "guild")
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private LocalDateTime creationDate;
    @Column
    private Boolean suspended;
    @Column
    private String suspendedReason;
    @ManyToMany
    @JsonIgnore
    private Set<User> admins;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Post> posts;
    @OneToMany
    @JsonIgnore
    private Set<GroupRequest> groupRequests;

    public Group(Integer id, String name, String description, LocalDateTime creationDate, Boolean isSuspended, String suspendedReason) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.suspended = isSuspended;
        this.suspendedReason = suspendedReason;
    }
}
