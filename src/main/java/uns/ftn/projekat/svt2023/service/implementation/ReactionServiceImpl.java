package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.model.enums.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @Override
    public void likePost(Integer postId) {
        User user = userService.returnLoggedUser();
        Post post = postService.findOne(postId);

        Reaction reaction = reactionRepository.findPostReaction(user.getId(), post.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setPost(post);
            reaction.setType(ReactionType.LIKE);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changePostReactionToLike(reaction.getId());
        }

    }

    @Override
    public void dislikePost(Integer postId) {
        User user = userService.returnLoggedUser();
        Post post = postService.findOne(postId);

        Reaction reaction = reactionRepository.findPostReaction(user.getId(), post.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setPost(post);
            reaction.setType(ReactionType.DISLIKE);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changePostReactionToDislike(reaction.getId());
        }
    }

    @Override
    public void hearthPost(Integer postId) {
        User user = userService.returnLoggedUser();
        Post post = postService.findOne(postId);

        Reaction reaction = reactionRepository.findPostReaction(user.getId(), post.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setPost(post);
            reaction.setType(ReactionType.HEARTH);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changePostReactionToHearth(reaction.getId());
        }
    }

    @Override
    public void deletePostReaction(Integer postId) {
        User loggedUser = userService.returnLoggedUser();
        reactionRepository.deletePostReaction(loggedUser.getId(), postId);
    }

    @Override
    public void likeComment(Integer commentId) {
        User user = userService.returnLoggedUser();
        Comment comment = commentService.findOne(commentId);

        Reaction reaction = reactionRepository.findCommentReaction(user.getId(), comment.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setComment(comment);
            reaction.setType(ReactionType.LIKE);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changeCommentReactionToLike(reaction.getId());
        }
    }

    @Override
    public void dislikeComment(Integer commentId) {
        User user = userService.returnLoggedUser();
        Comment comment = commentService.findOne(commentId);

        Reaction reaction = reactionRepository.findCommentReaction(user.getId(), comment.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setComment(comment);
            reaction.setType(ReactionType.DISLIKE);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changeCommentReactionToDislike(reaction.getId());
        }
    }

    @Override
    public void hearthComment(Integer commentId) {
        User user = userService.returnLoggedUser();
        Comment comment = commentService.findOne(commentId);

        Reaction reaction = reactionRepository.findCommentReaction(user.getId(), comment.getId());

        if(reaction == null) {
            reaction = new Reaction();
            reaction.setComment(comment);
            reaction.setType(ReactionType.HEARTH);
            reaction.setUser(user);
            reaction.setTimeStamp(LocalDateTime.now());

            reaction = reactionRepository.save(reaction);
        } else {
            reactionRepository.changeCommentReactionToHearth(reaction.getId());
        }
    }

    @Override
    public void deleteCommentReaction(Integer commentId) {
        User loggedUser = userService.returnLoggedUser();
        reactionRepository.deleteCommentReaction(loggedUser.getId(), commentId);
    }

    @Override
    public List<Integer> getCommentReactions(Integer commentId) {
        List<Integer> reactions = new ArrayList<>();
        reactions.add(reactionRepository.getCommentLikes(commentId));
        reactions.add(reactionRepository.getCommentDislikes(commentId));
        reactions.add(reactionRepository.getCommentHearths(commentId));

        return reactions;
    }

    @Override
    public List<Integer> getPostReactions(Integer postId) {
        List<Integer> reactions = new ArrayList<>();
        reactions.add(reactionRepository.getPostLikes(postId));
        reactions.add(reactionRepository.getPostDislikes(postId));
        reactions.add(reactionRepository.getPostHearths(postId));

        return reactions;
    }
}
