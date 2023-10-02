package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.transaction.*;
import java.util.List;

@Transactional
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    @Query(value = "SELECT r FROM Reaction r WHERE r.user.id = ?1 AND r.post.id = ?2")
    Reaction findPostReaction(Integer userId, Integer postId);

    @Query(value = "SELECT r FROM Reaction r WHERE r.user.id = ?1 AND r.comment.id = ?2")
    Reaction findCommentReaction(Integer userId, Integer commentId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=0 WHERE id=:reactionId")
    void changeCommentReactionToLike(Integer reactionId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=1 WHERE id=:reactionId")
    void changeCommentReactionToDislike(Integer reactionId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=2 WHERE id=:reactionId")
    void changeCommentReactionToHearth(Integer reactionId);

    @Modifying
    @Query(value = "DELETE Reaction WHERE user.id= ?1 AND comment.id = ?2")
    void deleteCommentReaction (Integer userId, Integer commentId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=0 WHERE id=:reactionId")
    void changePostReactionToLike(Integer reactionId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=1 WHERE id=:reactionId")
    void changePostReactionToDislike(Integer reactionId);

    @Modifying
    @Query(value = "UPDATE Reaction SET type=2 WHERE id=:reactionId")
    void changePostReactionToHearth(Integer reactionId);

    @Modifying
    @Query(value = "DELETE Reaction WHERE user.id= ?1 AND post.id = ?2")
    void deletePostReaction (Integer userId, Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 0 AND r.post.id = ?1")
    Integer getPostLikes (Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 1 AND r.post.id = ?1")
    Integer getPostDislikes (Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 2 AND r.post.id = ?1")
    Integer getPostHearths (Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 0 AND r.comment.id = ?1")
    Integer getCommentLikes (Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 1 AND r.comment.id = ?1")
    Integer getCommentDislikes (Integer postId);

    @Query(value = "SELECT COUNT(r) FROM Reaction r WHERE r.type = 2 AND r.comment.id = ?1")
    Integer getCommentHearths (Integer postId);
}
