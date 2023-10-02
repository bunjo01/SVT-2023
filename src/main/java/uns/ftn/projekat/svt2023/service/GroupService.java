package uns.ftn.projekat.svt2023.service;

import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.swing.text.html.*;
import java.util.*;

public interface GroupService {
    Group create(GroupDTO groupDTO);
    Group save(Group group);
    Optional<Group> delete(Integer id);
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
