package uns.ftn.projekat.svt2023.indexrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;

import java.util.Optional;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, String> {
    Optional<PostIndex> findByDatabaseId(Integer databaseId);

}
