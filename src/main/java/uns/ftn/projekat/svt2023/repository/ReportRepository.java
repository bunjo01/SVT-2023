package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.model.entity.Comment;
import uns.ftn.projekat.svt2023.model.entity.Post;
import uns.ftn.projekat.svt2023.model.entity.Report;
import uns.ftn.projekat.svt2023.model.entity.User;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query(value = "SELECT r FROM Report r WHERE r.reportedUser != null AND r.accepted = false")
    Set<Report> getAllReportedUsers();

    @Query(value = "SELECT r FROM Report r WHERE r.reportedUser != null AND r.accepted = true")
    Set<Report> getAllAcceptedUserReports();

    @Query(value = "SELECT r FROM Report r WHERE r.reportedComment != null AND r.accepted = false AND r.reportedComment.belongsToPost.group.id = null")
    Set<Report> getAllNonGroupReportedComments();

    @Query(value = "SELECT r FROM Report r WHERE r.reportedPost != null AND r.accepted = false and r.reportedPost.group.id = null")
    Set<Report> getAllNonGroupReportedPosts();

    @Query(value = "SELECT r FROM Report r WHERE r.reportedComment != null AND r.accepted = false AND r.reportedComment.belongsToPost.group.id = :groupId")
    Set<Report> getAllGroupReportedComments(Integer groupId);

    @Query(value = "SELECT r FROM Report r WHERE r.reportedPost != null AND r.accepted = false and r.reportedPost.group.id = :groupId")
    Set<Report> getAllGroupReportedPosts(Integer groupId);

    @Query(value = "SELECT u FROM User u WHERE username = :username")
    User getReportedUser(String username);

    @Query(value = "SELECT r FROM Report r WHERE r.accepted = true AND r.reportedUser.username = :username")
    Report getAcceptedReport(String username);

    @Modifying
    @Query(value = "UPDATE Report set accepted=true where id= :reportId")
    void acceptReport(Integer reportId);

    @Modifying
    @Query(value = "UPDATE Report set accepted=null where id= :reportId")
    void declineReport(Integer reportId);

    @Modifying
    @Query(value = "DELETE Report WHERE accepted = true AND id= :reportId")
    void deleteReport(Integer reportId);

    @Modifying
    @Query(value = "UPDATE User SET suspended = false WHERE id= :userId")
    void unblockUser(Integer userId);

    @Query(value = "SELECT r FROM Report r WHERE r.reportedUser.id = ?1 AND r.byUser.id = ?2")
    Report seeIfUserReportExists(Integer reportedUserId, Integer reportedByUserId);

    @Query(value = "SELECT r FROM Report r WHERE r.reportedPost.id = ?1 AND r.byUser.id = ?2")
    Report seeIfPostReportExists(Integer reportedPostId, Integer reportedByUserId);

    @Query(value = "SELECT r FROM Report r WHERE r.reportedComment.id = ?1 AND r.byUser.id = ?2")
    Report seeIfCommentReportExists(Integer reportedCommentId, Integer reportedByUserId);
}
