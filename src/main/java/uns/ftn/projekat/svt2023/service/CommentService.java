package uns.ftn.projekat.svt2023.service;

import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import java.util.*;

public interface CommentService {

    Comment create(CommentDTO commentDTO, Integer postId, Integer commentId);
    Comment edit(CommentDTO commentDTO, Integer commentId);
    Comment save(Comment comment);
    Comment delete(Integer id);
    Set<Comment> getAllPostComments(Integer postId);
    Set<Comment> getAllUserComments(Integer postId);
    Comment reply(CommentDTO commentDTO, Integer commentId);
    Comment findOne(Integer commentId);
    Set<Comment> getAllCommentReplies(Integer commentId);
    Set<Comment> getOldestComments(Integer postId);
    Set<Comment> getNewestComments(Integer postId);
    Set<Comment> getMostLikedComments(Integer postId);
    Set<Comment> getMostDislikedComments(Integer postId);
    Set<Comment> getMostHearthedComments(Integer postId);
    Set<Comment> getLeastLikedComments(Integer postId);
    Set<Comment> getLeastDislikedComments(Integer postId);
    Set<Comment> getLeastHearthedComments(Integer postId);
    void suspendComment(Integer commentId);
}
