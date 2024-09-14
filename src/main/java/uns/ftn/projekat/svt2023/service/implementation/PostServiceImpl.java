package uns.ftn.projekat.svt2023.service.implementation;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.MultipartFile;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;
import uns.ftn.projekat.svt2023.indexrepository.GroupIndexRepository;
import uns.ftn.projekat.svt2023.indexrepository.PostIndexRepository;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.pdf.PDFExtractor;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;
import uns.ftn.projekat.svt2023.searchservice.implementation.MinioService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.*;
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

    @Autowired
    private MinioService minioService;

    @Autowired
    private PostIndexRepository postIndexRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupIndexRepository groupIndexRepository;

    private final String postsBucketName = "posts";

    @Override
    public Post create(PostDTO postDTO, Integer groupId) {
        User postOwner = userService.returnLoggedUser();

        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            return null;
        }

        Group group = groupOptional.get();

        Post newPost = new Post();
        newPost.setCreationDate(LocalDateTime.now());
        newPost.setTitle(postDTO.getTitle());
        newPost.setContent(postDTO.getContent());
        newPost.setSuspended(false);
        newPost.setUser(postOwner);
        newPost.setGroup(group);

        // Save the post first to generate an ID
        newPost = postRepository.save(newPost);

        // Now handle the image if provided
        if (postDTO.getImagePath() != null && !postDTO.getImagePath().isEmpty()) {
            Image image = new Image();
            image.setPath(postDTO.getImagePath());
            image.setPost(newPost); // Set the post reference after the post is saved
            imageRepository.save(image);
            Set<Image> images = new HashSet<>();
            images.add(image);
            newPost.setImages(images);
        }

        // Save the post again to update it with the image reference
        newPost = postRepository.save(newPost);

        // Update the group index with the new post count
        GroupIndex groupIndex = groupIndexRepository.findByDatabaseId(groupId).orElse(null);
        if (groupIndex != null) {
            groupIndex.setNumberOfPosts(groupIndex.getNumberOfPosts() + 1);
            groupIndexRepository.save(groupIndex);
        }

        // Ensure PostIndex and Post have the same ID
//        PostIndex postIndex = new PostIndex();
//        postIndex.setDatabaseId(newPost.getId());
//        postIndex.setContent(newPost.getContent());
//        postIndex.setTitle(newPost.getTitle());
//        postIndex.setPdfDescriptionUrl("");
//        postIndex.setId(UUID.randomUUID().toString()); // Generate a new UUID for the PostIndex
//
//        postIndexRepository.save(postIndex);

        return newPost;
    }





    @Override
    public void savePostWithPdf(MultipartFile pdfFile, PostDTO postDTO, Integer groupId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Post post1 = this.create(postDTO, groupId);
        PostIndex postIndex = new PostIndex();
        postIndex.setDatabaseId(post1.getId());
        postIndex.setContent(post1.getContent());
        postIndex.setTitle(post1.getTitle());
        if (post1.getGroup() != null) {
            postIndex.setGroupId(post1.getGroup().getId());
        }

        postIndexRepository.save(postIndex);

        File file = convertToFile(pdfFile);

        String serverName = minioService.store(pdfFile);

        String pdfText = PDFExtractor.extractText(file);
        postIndex.setPdfText(pdfText);
        postIndex.setPdfDescriptionUrl(serverName);

        postIndexRepository.save(postIndex);
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> delete(Integer id) {
        Optional<Post> deletedPost = postRepository.findById(id);

        if (deletedPost.isEmpty()) {
            return Optional.empty();
        }
        postRepository.deleteById(id);

        return deletedPost;
    }

    @Override
    public Post findOne(Integer id) {
        return postRepository.findById(id).orElse(null);
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
        Set<Post> groupsPosts = new HashSet<>();

        for (Group group : userGroups) {
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
        return post.getGroup() != null;
    }

    @Override
    public Group getGroupByPostId(Integer postId) {
        return postRepository.getGroupByPostId(postId);
    }
}
