package uns.ftn.projekat.svt2023.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "groupAdmin")
@NoArgsConstructor
@Getter
@Setter
public class GroupAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


}
