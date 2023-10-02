package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.service.*;

import java.time.*;
import java.util.*;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserService userService;

    @Override
    public FriendRequest create(Integer toUserId) {

        User fromUser = userService.returnLoggedUser();
        User toUser = userService.findOne(toUserId);

        FriendRequest friendRequest = friendRequestRepository.findIfFriendRequestExists(fromUser.getId(), toUser.getId());

        if(friendRequest == null) {
            friendRequest = new FriendRequest();
            friendRequest.setCreatedAt(LocalDateTime.now());
            friendRequest.setApproved(null);
            friendRequest.setFromUser(fromUser);
            friendRequest.setToUser(toUser);
            FriendRequest createdFriendRequest = friendRequestRepository.save(friendRequest);

            return createdFriendRequest;
        }

        return null;
    }

    @Override
    public Set<FriendRequest> getAllUserFriendRequests(Integer userId) {return this.friendRequestRepository.getAllUserFriendRequests(userId);}

    @Override
    public void approveFriendRequest(Integer friendeRequestId) {this.friendRequestRepository.approveFriendRequest(friendeRequestId);}

    @Override
    public void declineFriendRequest(Integer friendeRequestId) {this.friendRequestRepository.declineFriendRequest(friendeRequestId);}
}
