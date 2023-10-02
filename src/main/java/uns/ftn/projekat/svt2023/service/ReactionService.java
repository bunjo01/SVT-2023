package uns.ftn.projekat.svt2023.service;

import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import java.util.List;

public interface ReactionService {
    void likePost(Integer postId);
    void dislikePost(Integer postId);
    void hearthPost(Integer postId);
    void deletePostReaction(Integer postId);
    void likeComment(Integer commentId);
    void dislikeComment(Integer commentId);
    void hearthComment(Integer commentId);
    void deleteCommentReaction(Integer commentId);
    List<Integer> getCommentReactions(Integer commentId);
    List<Integer> getPostReactions(Integer postId);
}
