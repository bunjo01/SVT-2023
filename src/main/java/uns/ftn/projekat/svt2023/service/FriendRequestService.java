package uns.ftn.projekat.svt2023.service;

import org.springframework.data.jpa.repository.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import java.util.*;

public interface FriendRequestService {
    FriendRequest create(Integer toUserId);
    Set<FriendRequest> getAllUserFriendRequests(Integer userId);
    void approveFriendRequest(Integer friendeRequestId);
    void declineFriendRequest(Integer friendeRequestId);
}
