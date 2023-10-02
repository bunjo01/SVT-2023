package uns.ftn.projekat.svt2023.service;

import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import java.util.Set;

public interface GroupRequestService {
    GroupRequest create(Integer groupId);
    void approveRequest(Integer requestId);
    void declineRequest(Integer requestId);
    GroupRequest returnRequestByUserAndGroups(Integer userId, Integer groupId);
    void deleteRequestByUserIdAndGroupId(Integer userId, Integer groupId);
    Set<Group> returnUserGroups(Integer userId);
}
