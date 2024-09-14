package uns.ftn.projekat.svt2023.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.swing.text.html.*;
import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import io.minio.MinioClient;



public interface GroupService {
    Group create(GroupDTO groupDTO);
    Group save(Group group);
    Optional<Group> delete(Integer id);
    void saveGroupWithPdf(MultipartFile pdfFile, GroupDTO groupDTO) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException;
    Group findOne(Integer id);
    Set<Group> getAllActiveGroups();
    Set<User> getAllGroupMembers(Integer id);
    Set<User> getAllGroupAdmins(Integer id);
    Set<Post> getAllGroupPosts(Integer id);
    Set<GroupRequest> getAllGroupRequests(Integer id);
    Set<Group> findUserGroups(Integer userId);
    Group suspendGroup(Integer groupId, String suspendReason);
    void addAdminToGroup(Integer groupId, Integer userId);
    void removeAdminFromGroup(Integer groupId, Integer userId);
    void banUserFromGroup(Integer userId, Integer groupId);
    void removeGroupMember(Integer userId, Integer groupId);
    void unblockMember(Integer userId, Integer groupId);
    Set<User> getAllBlockedUsers(Integer groupId);
}
