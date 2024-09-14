package uns.ftn.projekat.svt2023.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface PostService {
    Post create(PostDTO postDTO, Integer groupId);
    Post save(Post post);
    Optional<Post> delete(Integer id);
    Post findOne(Integer id);
    List<Post> findAll();
    Set<Post> getOldestPosts();
    Set<Post> getNewestPosts();
    void suspendPost(Integer postId);
    void addImageToPost(Integer postId, ImageDTO imageDTO);
    Set<Post> getAllPostsFromUserGroups();
    List<Post> getPostsForUser();
    Boolean isGroupPost(Integer postId);
    Group getGroupByPostId(Integer postId);
    void savePostWithPdf(MultipartFile pdfFile, PostDTO postDTO, Integer groupId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
