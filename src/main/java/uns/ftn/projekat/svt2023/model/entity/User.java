package uns.ftn.projekat.svt2023.model.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import uns.ftn.projekat.svt2023.model.enums.*;

import javax.persistence.*;
import java.time.*;
import java.util.*;


@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Roles role;
    @Column
    private Boolean suspended;
    @Column
    private LocalDateTime lastLogin;
    @OneToMany
    @JsonIgnore
    private Set<Post> posts;
    @ManyToMany
    @JsonIgnore
    private Set<Group> adminOfGroups;
    @OneToMany
    @JsonIgnore
    private Set<FriendRequest> friendRequests;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Reaction> reactions;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Comment> comments ;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Report> reports;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Banned> banned;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Banned> bannedUsers;
    @OneToOne(fetch = FetchType.LAZY)
    private Image image;


}
