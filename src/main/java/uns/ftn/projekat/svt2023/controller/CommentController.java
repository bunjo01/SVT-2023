package uns.ftn.projekat.svt2023.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.service.*;

import java.util.*;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    CommentService commentService;
    ReactionService reactionService;

    @Autowired
    public CommentController(CommentService commentService, ReactionService reactionService) {
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    @PostMapping("/{postId}/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> create(@RequestBody @Validated CommentDTO newComment, @PathVariable Integer postId){
        Comment createdComment = commentService.create(newComment, postId, null);

        if(createdComment == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        CommentDTO commentDTO = new CommentDTO(createdComment);

        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void delete(@PathVariable Integer id) {commentService.suspendComment(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> edit(@RequestBody CommentDTO editedComment) {
        Comment comment = commentService.edit(editedComment, editedComment.getId());

        if(editedComment == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        CommentDTO commentDTO = new CommentDTO(comment);

        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{commentId}/reply")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> reply(@RequestBody @Validated CommentDTO newComment, @PathVariable Integer commentId){
        Comment createdComment = commentService.reply(newComment, commentId);

        if(createdComment == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        CommentDTO commentDTO = new CommentDTO(createdComment);

        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}/replies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> commentReplies(@PathVariable Integer commentId) {
        return commentService.getAllCommentReplies(commentId);
    }

    @PostMapping("/{commentId}/like")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void likeComment(@PathVariable Integer commentId) {
        this.reactionService.likeComment(commentId);
    }

    @PostMapping("/{commentId}/dislike")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void dislikeComment(@PathVariable Integer commentId) {
        this.reactionService.dislikeComment(commentId);
    }

    @PostMapping("/{commentId}/hearth")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void hearthComment(@PathVariable Integer commentId) {
        this.reactionService.hearthComment(commentId);
    }

    @DeleteMapping("/{commentId}/deleteReaction")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteCommentReaction(@PathVariable Integer commentId) {
        this.reactionService.deleteCommentReaction(commentId);
    }

    @GetMapping("/{commentId}/reactions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Integer> getCommentReactions(@PathVariable Integer commentId) {
        return this.reactionService.getCommentReactions(commentId);
    }

    @GetMapping("/{postId}/oldest")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getOldestPosts(@PathVariable Integer postId) {
        return commentService.getOldestComments(postId);
    }

    @GetMapping("/{postId}/newest")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getNewestPosts(@PathVariable Integer postId) {
        return commentService.getNewestComments(postId);
    }

    @GetMapping("/{postId}/mostLiked")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getMostLikedComments(@PathVariable Integer postId) {
        return commentService.getMostLikedComments(postId);
    }

    @GetMapping("/{postId}/mostDisliked")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getMostDislikedComments(@PathVariable Integer postId) {
        return commentService.getMostDislikedComments(postId);
    }

    @GetMapping("/{postId}/mostHearthed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getMostHearthedComments(@PathVariable Integer postId) {
        return commentService.getMostHearthedComments(postId);
    }

    @GetMapping("/{postId}/leastLiked")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getLeastLikedComments(@PathVariable Integer postId) {
        return commentService.getLeastLikedComments(postId);
    }

    @GetMapping("/{postId}/leastDisliked")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getLeastDislikedComments(@PathVariable Integer postId) {
        return commentService.getLeastDislikedComments(postId);
    }

    @GetMapping("/{postId}/leastHearthed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getLeastHearthedComments(@PathVariable Integer postId) {
        return commentService.getLeastHearthedComments(postId);
    }


}
