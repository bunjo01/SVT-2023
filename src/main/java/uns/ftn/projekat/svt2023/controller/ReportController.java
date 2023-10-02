package uns.ftn.projekat.svt2023.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.GroupDTO;
import uns.ftn.projekat.svt2023.model.dto.ReportDTO;
import uns.ftn.projekat.svt2023.model.entity.Group;
import uns.ftn.projekat.svt2023.model.entity.Report;
import uns.ftn.projekat.svt2023.service.ReportService;

import java.util.Set;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/userReports")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Set<Report> getAllUserReports() {
        return reportService.getAllReportedUsers();
    }

    @GetMapping("/bannedUsers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Set<Report> getAllBannedUsers() {
        return reportService.getAllAcceptedUserReports();
    }

    @GetMapping("/nonGroupPosts")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Set<Report> getAllNonGroupReportedPosts() {
        return reportService.getAllNonGroupReportedPosts();
    }

    @GetMapping("/nonGroupComments")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Set<Report> getAllNonGroupReportedComments() {
        return reportService.getAllNonGroupReportedComments();
    }

    @GetMapping("/{groupId}/groupPosts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Report> getAllGroupReportedPosts(@PathVariable Integer groupId) {
        return reportService.getAllGroupReportedPosts(groupId);
    }

    @GetMapping("/{groupId}/groupComments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Report> getAllGroupReportedComments(@PathVariable Integer groupId) {
        return reportService.getAllGroupReportedComments(groupId);
    }

    @PostMapping("/{userUsername}/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void reportUser(@PathVariable String userUsername, @RequestBody ReportDTO reportDTO) {
        reportDTO.setUserUsername(userUsername);
        reportService.reportUser(reportDTO);
    }

    @PostMapping("/{commentId}/comment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void reportComment(@PathVariable Integer commentId,@RequestBody ReportDTO reportDTO) {
        reportDTO.setCommentId(commentId);
        reportService.reportComment(reportDTO);
    }

    @PostMapping("/{postId}/post")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void reportPost(@PathVariable Integer postId, @RequestBody ReportDTO reportDTO) {
        reportDTO.setPostId(postId);
        reportService.reportPost(reportDTO);
    }

    @PutMapping("/{reportId}/accept")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void acceptReport(@PathVariable Integer reportId) {
        reportService.acceptReport(reportId);
    }

    @PutMapping("/{reportId}/decline")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void declineReport(@PathVariable Integer reportId) {
        reportService.declineReport(reportId);
    }

    @PutMapping("/{username}/unblock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void unblockUser(@PathVariable String username) {
        reportService.unblockUser(username);
    }
}
