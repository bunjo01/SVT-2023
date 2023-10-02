package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.transaction.*;
import java.util.*;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Set<Post> findByUser(User user);

    @Query(value = "SELECT p FROM Post p JOIN GroupRequest gr WHERE gr.user.id = ?1 AND gr.approved = true AND p.user.id = ?1 AND p.suspended = false AND p.group.id = gr.group.id")
    Set<Post> getAllPostsFromUserGroups(Integer userId);

    @Query(value = "SELECT p FROM Post p WHERE p.suspended = false AND p.group.id = null")
    Set<Post> getAllNonGroupPosts();

    @Query(value = "Select p from Post p WHERE p.suspended = false order by p.creationDate asc")
    Set<Post> findOldest();

    @Query(value = "Select p from Post p WHERE p.suspended = false order by p.creationDate desc")
    Set<Post> findNewest();

    @Modifying
    @Query(value = "UPDATE Post set suspended=true where id= :postId")
    void suspendPost(Integer postId);

    @Query(value = "SELECT p.group FROM Post p WHERE p.id = ?1 AND p.suspended = false")
    Group getGroupByPostId(Integer postId);

}
