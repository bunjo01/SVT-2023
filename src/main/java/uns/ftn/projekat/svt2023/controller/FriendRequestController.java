package uns.ftn.projekat.svt2023.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.service.*;

import javax.transaction.*;

@Transactional
@RestController
@RequestMapping("api/friend_requests")
public class FriendRequestController {

    FriendRequestService friendRequestService;
    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/{toUserId}/send")
    public void sendFriendRequest(@PathVariable Integer toUserId) {
        friendRequestService.create(toUserId);
    }

    @PostMapping("/{friendRequestId}/approve")
    public void approveFriendRequest(@PathVariable Integer friendRequestId) {
        friendRequestService.approveFriendRequest(friendRequestId);
    }

    @PostMapping("/{friendRequestId}/decline")
    public void declineFriendRequest(@PathVariable Integer friendRequestId) {
        friendRequestService.declineFriendRequest(friendRequestId);
    }

}
