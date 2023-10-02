package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRequestService groupRequestService;
    @Autowired
    private ImageRepository imageRepository;


    @Override
    public Post create(PostDTO postDTO, Integer groupId) {

        if(postDTO.getImagePath() == null) {
            postDTO.setImagePath("");
        }

        User user = userService.returnLoggedUser();
        Image createdImage = new Image();

        Post newPost = new Post();
        newPost.setSuspended(false);
        newPost.setUser(user);
        newPost.setCreationDate(LocalDateTime.now());
        newPost.setId(postDTO.getId());
        newPost.setContent(postDTO.getContent());

        if(!postDTO.getImagePath().equals("")) {
            Image image = new Image();
            image.setPath(postDTO.getImagePath());
            createdImage = imageRepository.save(image);
            Set<Image> images = new HashSet<>();
            images.add(image);
            newPost.setImages(images);
        }

        if(groupId != null) {
            Group group = groupService.findOne(groupId);
            newPost.setGroup(group);
        }

        newPost = postRepository.save(newPost);

        if(!postDTO.getImagePath().equals("")) {
            createdImage.setPost(newPost);
            imageRepository.save(createdImage);
        }


        return newPost;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> delete(Integer id) {
        Optional<Post> deletedPost = postRepository.findById(id);

        if(deletedPost == null) {
            return null;
        }
        postRepository.deleteById(id);

        return deletedPost;
    }

    @Override
    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    @Override
    public Set<Post> getOldestPosts() {
        return postRepository.findOldest();
    }

    @Override
    public Set<Post> getNewestPosts() {
        return postRepository.findNewest();
    }

    @Override
    public void suspendPost(Integer postId) {
        postRepository.suspendPost(postId);
    }

    @Override
    public void addImageToPost(Integer postId, ImageDTO imageDTO) {
        Post post = findOne(postId);
        post.setImages(imageRepository.getAllPostImages(postId));

        Image newImage = new Image();
        newImage.setPath(imageDTO.getPath());
        newImage.setPost(post);

        imageRepository.save(newImage);
        post.getImages().add(newImage);
        save(post);
    }

    @Override
    public Set<Post> getAllPostsFromUserGroups() {
        User loggedUser = userService.returnLoggedUser();
        Set<Group> userGroups = groupRequestService.returnUserGroups(loggedUser.getId());
        Set<Post> groupsPosts =  new HashSet<>();

        for(Group group : userGroups) {
            group.setPosts(groupService.getAllGroupPosts(group.getId()));
            groupsPosts.addAll(group.getPosts());
        }

        return groupsPosts;
    }

    @Override
    public List<Post> getPostsForUser() {
        User user = userService.returnLoggedUser();
        Set<Post> fromGroups = getAllPostsFromUserGroups();
        Set<Post> nonGroupPosts = postRepository.getAllNonGroupPosts();

        Set<Post> postsForReturn = new HashSet<>();
        postsForReturn.addAll(fromGroups);
        postsForReturn.addAll(nonGroupPosts);

        List<Post> returnArray = new ArrayList<>(postsForReturn);

        Collections.shuffle(returnArray);

        return returnArray;
    }

    @Override
    public Boolean isGroupPost(Integer postId) {
        Post post = findOne(postId);
        return post.getGroup() == null;
    }

    @Override
    public Group getGroupByPostId(Integer postId) {
        return postRepository.getGroupByPostId(postId);
    }


}
