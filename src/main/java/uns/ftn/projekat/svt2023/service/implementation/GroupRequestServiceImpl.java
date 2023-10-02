package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.time.*;
import java.util.Set;

@Service
public class GroupRequestServiceImpl implements GroupRequestService {

    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private GroupService groupService;

    @Override
    public GroupRequest create(Integer groupId) {
        Group group = groupService.findOne(groupId);
        User user = userService.returnLoggedUser();

        if(group == null || user == null) {
            return null;
        }

        GroupRequest groupRequest = new GroupRequest();

        groupRequest.setGroup(group);
        groupRequest.setUser(user);
        groupRequest.setApproved(null);
        groupRequest.setCreated(LocalDateTime.now());

        return groupRequestRepository.save(groupRequest);
    }


    @Override
    public void approveRequest(Integer requestId) {groupRequestRepository.approveGroupRequest(requestId);}

    @Override
    public void declineRequest(Integer requestId) {groupRequestRepository.declineGroupRequest(requestId);}

    @Override
    public GroupRequest returnRequestByUserAndGroups(Integer userId, Integer groupId) {
        return groupRequestRepository.returnRequestByUserAndGroup(userId, groupId);
    }

    @Override
    public void deleteRequestByUserIdAndGroupId(Integer userId, Integer groupId) {
        groupRequestRepository.deleteGroupRequestByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public Set<Group> returnUserGroups(Integer userId) {
        return groupRequestRepository.returnUserGroups(userId);
    }

}
