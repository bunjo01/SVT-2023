package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ftn.projekat.svt2023.model.dto.ReportDTO;
import uns.ftn.projekat.svt2023.model.entity.Comment;
import uns.ftn.projekat.svt2023.model.entity.Post;
import uns.ftn.projekat.svt2023.model.entity.Report;
import uns.ftn.projekat.svt2023.model.entity.User;
import uns.ftn.projekat.svt2023.model.enums.ReportReason;
import uns.ftn.projekat.svt2023.repository.ReportRepository;
import uns.ftn.projekat.svt2023.service.CommentService;
import uns.ftn.projekat.svt2023.service.PostService;
import uns.ftn.projekat.svt2023.service.ReportService;
import uns.ftn.projekat.svt2023.service.UserService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    UserService userService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    PostService postService;

    @Override
    public void reportUser(ReportDTO reportDTO) {
        User reportedUser = userService.findByUsername(reportDTO.getUserUsername());
        User reportedBy = userService.returnLoggedUser();

        Report report = reportRepository.seeIfUserReportExists(reportedUser.getId(), reportedBy.getId());

        if(report == null) {
            report = new Report();
            report.setReportedUser(reportedUser);
            report.setByUser(reportedBy);
            report.setAccepted(false);
            report.setTimeStamp(LocalDateTime.now());
            report.setReason(ReportReason.valueOf(reportDTO.getReason()));

            reportRepository.save(report);
        }
    }

    @Override
    public void reportComment(ReportDTO reportDTO) {

        Comment reportedComment = commentService.findOne(reportDTO.getCommentId());
        User reportedBy = userService.returnLoggedUser();

        Report report = reportRepository.seeIfCommentReportExists(reportedComment.getId(), reportedBy.getId());

        if(report == null){
            report = new Report();
            report.setReportedComment(reportedComment);
            report.setByUser(reportedBy);
            report.setAccepted(false);
            report.setTimeStamp(LocalDateTime.now());
            report.setReason(ReportReason.valueOf(reportDTO.getReason()));

            reportRepository.save(report);
        }
    }

    @Override
    public void reportPost(ReportDTO reportDTO) {
        Post reportedPost = postService.findOne(reportDTO.getPostId());
        User reportedBy = userService.returnLoggedUser();

        Report report = reportRepository.seeIfPostReportExists(reportedPost.getId(), reportedBy.getId());

        if(report == null) {
            report = new Report();
            report.setReportedPost(reportedPost);
            report.setByUser(reportedBy);
            report.setAccepted(false);
            report.setTimeStamp(LocalDateTime.now());
            report.setReason(ReportReason.valueOf(reportDTO.getReason()));

            reportRepository.save(report);
        }

    }

    @Override
    public void acceptReport(Integer reportId) {
        reportRepository.acceptReport(reportId);
        Report report = reportRepository.findById(reportId).orElseGet(null);

        if(report != null) {
            if(report.getReportedUser() != null) {
                userService.suspendUser(report.getReportedUser().getUsername());
            } else if (report.getReportedComment() != null) {
                commentService.suspendComment(report.getReportedComment().getId());
            } else if (report.getReportedPost() != null) {
                postService.suspendPost(report.getReportedPost().getId());
            }
        }
    }

    @Override
    public void declineReport(Integer reportId) {
        reportRepository.declineReport(reportId);
    }

    @Override
    public void unblockUser(String username) {
        User user = reportRepository.getReportedUser(username);
        Report report = reportRepository.getAcceptedReport(username);
        reportRepository.deleteReport(report.getId());
        reportRepository.unblockUser(user.getId());
    }

    @Override
    public Set<Report> getAllReportedUsers() {
        return reportRepository.getAllReportedUsers();
    }

    @Override
    public Set<Report> getAllAcceptedUserReports() {
        return reportRepository.getAllAcceptedUserReports();
    }

    @Override
    public Set<Report> getAllNonGroupReportedComments() {
        return reportRepository.getAllNonGroupReportedComments();
    }

    @Override
    public Set<Report> getAllNonGroupReportedPosts() {
        return reportRepository.getAllNonGroupReportedPosts();
    }

    @Override
    public Set<Report> getAllGroupReportedComments(Integer groupId) {
        return reportRepository.getAllGroupReportedComments(groupId);
    }

    @Override
    public Set<Report> getAllGroupReportedPosts(Integer groupId) {
        return reportRepository.getAllGroupReportedPosts(groupId);
    }


}
