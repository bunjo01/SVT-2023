package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.model.entity.Banned;
import uns.ftn.projekat.svt2023.model.entity.User;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface BannedRepository extends JpaRepository<Banned, Integer> {

    @Query(value = "SELECT b.bannedUser FROM Banned b WHERE b.bannedFromGroup.id = :groupId")
    Set<User> returnBannedUsersFromGroup(Integer groupId);

    @Modifying
    @Query("DELETE Banned WHERE bannedUser.id = ?1 AND bannedFromGroup.id = ?2")
    void unblockUserFromGroup(Integer userId, Integer groupId);
}
