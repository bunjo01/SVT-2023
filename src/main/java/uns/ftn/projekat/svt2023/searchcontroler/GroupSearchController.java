package uns.ftn.projekat.svt2023.searchcontroler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.indexmodel.GroupIndex;
import uns.ftn.projekat.svt2023.model.searchDTO.SearchQueryDTO;
import uns.ftn.projekat.svt2023.searchservice.GroupSearchService;

@RestController
@RequestMapping("api/search/groups")
@RequiredArgsConstructor
public class GroupSearchController {

    private final GroupSearchService groupSearchService;

    @PostMapping("/simple")
    public Page<GroupIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
        return groupSearchService.groupSimpleSearch(simpleSearchQuery.keywords(), pageable);
    }


    @PostMapping("/advanced")
    public Page<GroupIndex> advancedSearch(@RequestBody SearchQueryDTO searchQuery, Pageable pageable) {
        return groupSearchService.groupAdvancedSearch(searchQuery.keywords(), pageable);
    }
}
