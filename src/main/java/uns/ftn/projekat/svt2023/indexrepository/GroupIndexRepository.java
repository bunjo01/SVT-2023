package uns.ftn.projekat.svt2023.indexrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;

import java.util.Optional;

@Repository
public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
    Optional<GroupIndex> findByDatabaseId(Integer databaseId);
}
