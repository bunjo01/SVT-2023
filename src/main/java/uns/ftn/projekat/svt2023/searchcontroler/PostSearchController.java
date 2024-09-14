package uns.ftn.projekat.svt2023.searchcontroler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;
import uns.ftn.projekat.svt2023.model.searchDTO.SearchQueryDTO;
import uns.ftn.projekat.svt2023.searchservice.PostSearchService;

@RestController
@RequestMapping("api/search/posts")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    @PostMapping("/simple")
    public Page<PostIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
        return postSearchService.postSimpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<PostIndex> advancedSearch(@RequestBody SearchQueryDTO searchQuery, Pageable pageable) {
        return postSearchService.postAdvancedSearch(searchQuery.keywords(), pageable);
    }
}
