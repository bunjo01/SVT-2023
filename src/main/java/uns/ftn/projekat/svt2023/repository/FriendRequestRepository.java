package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.transaction.*;
import java.util.*;

@Repository
@Transactional
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    @Query(value = "SELECT r FROM FriendRequest r WHERE r.approved = null AND r.toUser.id = :userId")
    Set<FriendRequest> getAllUserFriendRequests(Integer userId);

    @Query(value = "SELECT r FROM FriendRequest r WHERE r.fromUser.id = ?1  AND r.toUser.id = ?2")
    FriendRequest findIfFriendRequestExists(Integer sendById, Integer toUserId);

    @Modifying
    @Query(value = "UPDATE FriendRequest set approved=true where id= :friendRequestId")
    void approveFriendRequest(Integer friendRequestId);

    @Modifying
    @Query(value = "UPDATE FriendRequest set approved=false where id= :friendRequestId")
    void declineFriendRequest(Integer friendRequestId);
}
