package uns.ftn.projekat.svt2023.service;

import uns.ftn.projekat.svt2023.model.dto.ReportDTO;
import uns.ftn.projekat.svt2023.model.entity.Comment;
import uns.ftn.projekat.svt2023.model.entity.Post;
import uns.ftn.projekat.svt2023.model.entity.Report;
import uns.ftn.projekat.svt2023.model.entity.User;

import java.util.Set;

public interface ReportService {

    void reportUser(ReportDTO reportDTO);
    void reportComment(ReportDTO reportDTO);
    void reportPost(ReportDTO reportDTO);
    void acceptReport(Integer reportId);
    void declineReport(Integer reportId);
    void unblockUser(String username);
    Set<Report> getAllReportedUsers();
    Set<Report> getAllAcceptedUserReports();
    Set<Report> getAllNonGroupReportedComments();
    Set<Report> getAllNonGroupReportedPosts();
    Set<Report> getAllGroupReportedComments(Integer groupId);
    Set<Report> getAllGroupReportedPosts(Integer groupId);

}
