package uns.ftn.projekat.svt2023.searchservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;

import java.util.List;

public interface GroupSearchService {
    Page<GroupIndex> groupSimpleSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> groupAdvancedSearch(List<String> keywords, Pageable pageable);
}
