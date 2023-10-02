package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.*;

import javax.transaction.*;
import java.util.*;

@Transactional
@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Integer> {
    @Modifying
    @Query(value = "UPDATE GroupRequest set approved=true where id= :groupRequestId")
    void approveGroupRequest(Integer groupRequestId);

    @Modifying
    @Query(value = "UPDATE GroupRequest set approved=false where id= :groupRequestId")
    void declineGroupRequest(Integer groupRequestId);

    @Query(value = "SELECT g FROM GroupRequest r JOIN r.group g where r.id = :groupRequestId")
    Group findGroupByGroupRequestId(Integer groupRequestId);

    @Query(value = "SELECT u FROM GroupRequest r JOIN r.user u where r.id = :groupRequestId")
    User findUserByGroupRequestId(Integer groupRequestId);

    @Query(value = "SELECT r FROM GroupRequest r WHERE r.user.id = ?1 AND r.group.id = ?2")
    GroupRequest returnRequestByUserAndGroup(Integer userId, Integer groupId);

    @Query(value = "SELECT r.group FROM GroupRequest r WHERE r.user.id = ?1 AND r.approved = true")
    Set<Group> returnUserGroups(Integer userId);

    @Modifying
    @Query(value = "DELETE GroupRequest WHERE user.id = ?1 AND group.id = ?2 ")
    void deleteGroupRequestByUserIdAndGroupId(Integer userId, Integer groupId);
}
