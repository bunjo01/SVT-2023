package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private BannedRepository bannedRepository;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private GroupRequestService groupRequestService;

    @Override
    public Group create(GroupDTO groupDTO) {
        User groupOwner = userService.returnLoggedUser();

        Set<User> admins = new HashSet<User>();
        admins.add(groupOwner);

        Group newGroup = new Group();
        newGroup.setCreationDate(LocalDateTime.now());
        newGroup.setId(groupDTO.getId());
        newGroup.setName(groupDTO.getName());
        newGroup.setDescription(groupDTO.getDescription());
        newGroup.setSuspended(false);
        newGroup.setSuspendedReason("");
        newGroup.setAdmins(admins);
        newGroup = groupRepository.save(newGroup);

        GroupRequest groupRequest = groupRequestService.create(newGroup.getId());
        groupRequestService.approveRequest(groupRequest.getId());

        return newGroup;
    }

    @Override
    public Group save(Group group) {return groupRepository.save(group);}

    @Override
    public Optional<Group> delete(Integer id) {
        Optional<Group> deletedGroup = groupRepository.findById(id);
        if(deletedGroup == null) {
            return null;
        }
        groupRepository.deleteById(id);

        return deletedGroup;
    }

    @Override
    public Group findOne(Integer id) {return groupRepository.findById(id).orElseGet(null);}

    @Override
    public Set<Group> getAllActiveGroups() {return groupRepository.getAllGroupActiveGroups();}

    @Override
    public Set<User> getAllGroupMembers(Integer id) {return groupRepository.getAllGroupMembers(id);}

    @Override
    public Set<User> getAllGroupAdmins(Integer id) {return groupRepository.getAllGroupAdmins(id);}

    @Override
    public Set<Post> getAllGroupPosts(Integer id) {return groupRepository.getAllGroupPosts(id);}

    @Override
    public Set<GroupRequest> getAllGroupRequests(Integer id) {return groupRepository.getAllGroupRequests(id);}

    @Override
    public Set<Group> findUserGroups(Integer userId) {return groupRepository.getAllUserGroups(userId);}

    @Override
    public Group suspendGroup(Integer groupId, String suspendReason) {
        groupRepository.suspendGroup(groupId, suspendReason);

        Set<User> emptySet = new HashSet<>();

        Group group = this.findOne(groupId);
        group.setAdmins(emptySet);
        group = groupRepository.save(group);

        return group;
    }

    @Override
    public void addAdminToGroup(Integer groupId, Integer userId) {
        Group group = this.findOne(groupId);
        User user = userService.findOne(userId);

        group.setAdmins(this.getAllGroupAdmins(groupId));
        group.getAdmins().add(user);

        this.save(group);
    }

    @Override
    public void removeAdminFromGroup(Integer groupId, Integer userId) {
        Group group = this.findOne(groupId);
        User user = userService.findOne(userId);

        Set<User> emptySet = new HashSet<>();
        group.setAdmins(this.getAllGroupAdmins(groupId));
        Set<User> groupAdmins = group.getAdmins();

        group.setAdmins(emptySet);
        groupRepository.save(group);

        Set<User> newAdmins = new HashSet<>();

        for (User admin: groupAdmins) {
            if(!admin.getUsername().equals(user.getUsername())) {
                newAdmins.add(admin);
            }
        }

        group.setAdmins(newAdmins);
        groupRepository.save(group);
    }

    @Override
    public void banUserFromGroup(Integer userId, Integer groupId) {
        User bannedUser = userService.findOne(userId);
        Group group = findOne(groupId);
        User bannedBy = userService.returnLoggedUser();

        Banned banned = new Banned();
        banned.setBannedBy(bannedBy);
        banned.setBannedFromGroup(group);
        banned.setBannedUser(bannedUser);
        banned.setTimeStamp(LocalDateTime.now());

        removeGroupMember(userId, groupId);

        bannedRepository.save(banned);
    }

    @Override
    public void removeGroupMember(Integer userId, Integer groupId) {
        User user = userService.findOne(userId);
        Group group = findOne(groupId);

        group.setAdmins(getAllGroupAdmins(groupId));

        if(group.getAdmins().contains(user)) {
            Set<User> emptySet = new HashSet<>();
            group.setAdmins(this.getAllGroupAdmins(groupId));
            Set<User> groupAdmins = group.getAdmins();

            group.setAdmins(emptySet);
            groupRepository.save(group);

            Set<User> newAdmins = new HashSet<>();

            for (User admin: groupAdmins) {
                if(!admin.getUsername().equals(user.getUsername())) {
                    newAdmins.add(admin);
                }
            }

            group.setAdmins(newAdmins);
            groupRepository.save(group);
        }

        groupRepository.deleteSuspendedUserPosts(userId, groupId);
        groupRequestService.deleteRequestByUserIdAndGroupId(userId, groupId);

    }

    @Override
    public void unblockMember(Integer userId, Integer groupId) {
        this.bannedRepository.unblockUserFromGroup(userId, groupId);
    }

    @Override
    public Set<User> getAllBlockedUsers(Integer groupId) {
        return this.bannedRepository.returnBannedUsersFromGroup(groupId);
    }

}
