package uns.ftn.projekat.svt2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.model.entity.Image;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i FROM Image i WHERE i.post.id = :postId")
    Set<Image> getAllPostImages(Integer postId);

    @Query("SELECT i FROM Image i WHERE i.user.id = ?1")
    Image getUserImage(Integer userId);



}
