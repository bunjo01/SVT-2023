package uns.ftn.projekat.svt2023.controller;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import javax.persistence.criteria.*;
import java.util.*;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    GroupService groupService;
    PostService postService;

    @Autowired
    public GroupController(GroupService groupService, PostService postService) {
        this.groupService = groupService;
        this.postService = postService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> create(@RequestBody GroupDTO newGroup) {

        Group createdGroup = groupService.create(newGroup);

        if(createdGroup == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        GroupDTO groupDTO = new GroupDTO(createdGroup);

        return new ResponseEntity<>(groupDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void delete(@PathVariable Integer id) {
       groupService.suspendGroup(id, "deleted");
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> edit(@RequestBody @Validated GroupDTO editGroup){
        Group edit = groupService.findOne(editGroup.getId());
        edit.setDescription(editGroup.getDescription());
        edit.setName(editGroup.getName());
        groupService.save(edit);

        GroupDTO groupDTO = new GroupDTO(edit);
        return  new ResponseEntity<>(groupDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/members")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<User> getGroupMembers(@PathVariable Integer groupId) {
        return groupService.getAllGroupMembers(groupId);
    }

    @PostMapping("/{groupId}/admins/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void addAdminToGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.addAdminToGroup(groupId, userId);
    }

    @DeleteMapping("/{groupId}/admins/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void removeAdminFromGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.removeAdminFromGroup(groupId, userId);
    }

    @GetMapping("/{groupId}/admins")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<User> getGroupAdmins(@PathVariable Integer groupId) {
        return groupService.getAllGroupAdmins(groupId);
    }

    @GetMapping("/{groupId}/posts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getGroupPosts(@PathVariable Integer groupId) {
        return groupService.getAllGroupPosts(groupId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Group> loadAll() {return this.groupService.getAllActiveGroups();}

    @GetMapping("/{groupId}")
    public GroupDTO getGroup(@PathVariable Integer groupId) {
        Group group = groupService.findOne(groupId);
        GroupDTO groupDTO = new GroupDTO(group);
        return groupDTO;
    }

    @PostMapping("/{groupId}/posts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> createGroupPost(@PathVariable Integer groupId, @RequestBody PostDTO newPost) {

        Post createdPost = postService.create(newPost, groupId);

        if(createdPost == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/requests")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<GroupRequest> getGroupRequests(@PathVariable Integer groupId) {return groupService.getAllGroupRequests(groupId);}
    
    @PostMapping("/{groupId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuspendDTO> banGroup(@PathVariable Integer groupId, @RequestBody SuspendDTO suspendDTO) {
        String suspendReason = suspendDTO.getDescription();
        Group group = groupService.suspendGroup(groupId, suspendReason);
        SuspendDTO returnSuspendDTO = new SuspendDTO(group);

        if(returnSuspendDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(returnSuspendDTO, HttpStatus.CREATED);

    }

    @PostMapping("/{groupId}/remove/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void removeMember(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.banUserFromGroup(userId, groupId);
    }

    @GetMapping("/{groupId}/blockedMembers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<User> getBlockedMembers(@PathVariable Integer groupId) {
        return groupService.getAllBlockedUsers(groupId);
    }

    @PostMapping("/{groupId}/unblock/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void unblockMember(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.unblockMember(userId, groupId);
    }

}
