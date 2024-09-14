package uns.ftn.projekat.svt2023.searchservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;

import java.util.List;

public interface PostSearchService {
    Page<PostIndex> postSimpleSearch(List<String> keywords, Pageable pageable);
    Page<PostIndex> postAdvancedSearch(List<String> keywords, Pageable pageable);
}
