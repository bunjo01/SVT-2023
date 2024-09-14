package uns.ftn.projekat.svt2023.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("api/posts")
public class PostController {

    PostService postService;
    CommentService commentService;
    ReactionService reactionService;


    @Autowired
    public PostController(PostService postService, CommentService commentService, ReactionService reactionService) {
        this.postService = postService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    @PostMapping("/add/{groupId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> addPostWithPdf(@PathVariable Integer groupId, @RequestParam("pdf") MultipartFile pdfFile, @RequestParam("post") String postJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PostDTO postDTO = objectMapper.readValue(postJson, PostDTO.class);

//            Post createdPost = postService.create(postDTO, groupId);
//
//            if (createdPost == null) {
//                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
//            }

            // Ensure PostIndex and Post have the same ID

            postService.savePostWithPdf(pdfFile, postDTO, groupId);

            return ResponseEntity.ok(null);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            return ResponseEntity.status(500).body("Error saving post or PDF file: " + e.getMessage());
        }
    }





    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO newPost) {

        Post createdPost = postService.create(newPost, null);

        if(createdPost == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void delete(@RequestParam Integer id) {
        postService.suspendPost(id);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> edit(@RequestBody @Validated PostDTO editPost){
        Post edit = postService.findOne(editPost.getId());
        edit.setContent(editPost.getContent());
        postService.save(edit);

        PostDTO postDTO = new PostDTO(edit);
        return  new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}/replies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getCommentReplies(@PathVariable Integer commentId) {
        return commentService.getAllCommentReplies(commentId);
    }

    @GetMapping("/{postId}/getGroup")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Group getGroupByPostId(@PathVariable Integer postId) {
       return postService.getGroupByPostId(postId);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Post getPost(@PathVariable Integer postId) {
        return postService.findOne(postId);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Post> loadAll() {
        return postService.findAll();
    }

    @GetMapping("/postsFromUserGroups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getAllPostsFromUserGroups() {
        return postService.getAllPostsFromUserGroups();
    }

    @GetMapping("/postsForUser")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Post> getPostsForUser() {
        return postService.getPostsForUser();
    }

    @GetMapping("/{postId}/isGroupPost")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public boolean getPostsForUser(@PathVariable Integer postId) {
        return postService.isGroupPost(postId);
    }

    @GetMapping("/{postId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Comment> getAllPostComments(@PathVariable Integer postId) {
        return commentService.getAllPostComments(postId);
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void likePost(@PathVariable Integer postId) {
        this.reactionService.likePost(postId);
    }

    @PostMapping("/{postId}/dislike")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void dislikePost(@PathVariable Integer postId) {
        this.reactionService.dislikePost(postId);
    }

    @PostMapping("/{postId}/hearth")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void hearthPost(@PathVariable Integer postId) {
        this.reactionService.hearthPost(postId);
    }

    @DeleteMapping("/{postId}/deleteReaction")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deletePostReaction(@PathVariable Integer postId) {
        this.reactionService.deletePostReaction(postId);
    }

    @GetMapping("/{postId}/reactions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Integer> getPostReactions(@PathVariable Integer postId) {
        return this.reactionService.getPostReactions(postId);
    }

    @GetMapping("/oldest")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getOldestPosts() {
        return postService.getOldestPosts();
    }

    @GetMapping("/newest")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getNewestPosts() {
        return postService.getNewestPosts();
    }

    @PostMapping("/{postId}/image")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void addImageToPost(@PathVariable Integer postId, @RequestBody ImageDTO imageDTO) {
        postService.addImageToPost(postId, imageDTO);
    }
}
