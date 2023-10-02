package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.transaction.Transactional;
import java.util.*;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT c FROM Comment c WHERE c.belongsToPost.id = :postId")
    Set<Comment> getAllPostComments(Integer postId);
    @Query(value = "SELECT c FROM Comment c WHERE c.suspended = false AND c.belongsToUser.id = :userId")
    Set<Comment> getAllUserComments(Integer userId);
    @Query(value = "SELECT c FROM Comment c WHERE c.suspended = false AND c.repliesToComment.id = :commentId")
    Set<Comment> getAllCommentReplies(Integer commentId);
    @Query(value = "Select c from Comment c WHERE c.belongsToPost.id = ?1 order by c.timeStamp asc")
    Set<Comment> findOldest(Integer postId);
    @Query(value = "Select c from Comment c WHERE c.belongsToPost.id = ?1 order by c.timeStamp desc")
    Set<Comment> findNewest(Integer postId);
    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 0 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) DESC")
    Set<Comment> getMostLikedComments(Integer postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 1 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) DESC")
    Set<Comment> getMostDislikedComments(Integer postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 2 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) DESC")
    Set<Comment> getMostHearthedComments(Integer postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 0 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) ASC")
    Set<Comment> getLeastLikedComments(Integer postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 1 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) ASC")
    Set<Comment> getLeastDislikedComments(Integer postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN Reaction r ON r.type = 2 AND c.id = r.comment.id " +
            "WHERE c.belongsToPost.id = ?1 " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r.id) ASC")
    Set<Comment> getLeastHearthedComments(Integer postId);

    @Modifying
    @Query(value = "UPDATE Comment set suspended=true where id= :commentId")
    void suspendComment(Integer commentId);

}
