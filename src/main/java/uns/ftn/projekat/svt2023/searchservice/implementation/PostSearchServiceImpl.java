package uns.ftn.projekat.svt2023.searchservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import uns.ftn.projekat.svt2023.indexmodel.PostIndex;
import uns.ftn.projekat.svt2023.searchservice.PostSearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private static final Logger log = LoggerFactory.getLogger(PostSearchServiceImpl.class);
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<PostIndex> postSimpleSearch(List<String> keywords, Pageable pageable) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords must not be null or empty");
        }

        Query searchQuery = buildSimpleSearchQuery(keywords);
        log.info("Executing search query: {}", searchQuery.toString());

        var searchQueryBuilder = new NativeQueryBuilder().withQuery(searchQuery).withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> postAdvancedSearch(List<String> expression, Pageable pageable) {
        if (expression == null || expression.size() != 3) {
            throw new IllegalArgumentException("Search query malformed.");
        }

        String operation = expression.get(1);
        expression.remove(1);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation)).withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token).analyzer("serbian")));
                b.should(sb -> sb.match(m -> m.field("content").query(token).analyzer("serbian")));
                b.should(sb -> sb.match(m -> m.field("pdf_text").query(token).analyzer("serbian")));
            });
            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];
            var field2 = operands.get(1).split(":")[0];
            var value2 = operands.get(1).split(":")[1];

            switch (operation) {
                case "AND":
                    b.must(sb -> sb.match(m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1).analyzer("serbian")));
                    b.must(sb -> sb.match(m -> m.field(field2).query(value2).analyzer("serbian")));
                    break;
                case "OR":
                    b.should(sb -> sb.match(m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1).analyzer("serbian")));
                    b.should(sb -> sb.match(m -> m.field(field2).query(value2).analyzer("serbian")));
                    break;
                case "NOT":
                    b.must(sb -> sb.match(m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1).analyzer("serbian")));
                    b.mustNot(sb -> sb.match(m -> m.field(field2).query(value2).analyzer("serbian")));
                    break;
            }

            return b;
        })))._toQuery();
    }

    private Page<PostIndex> runQuery(NativeQuery searchQuery) {
        log.info("Executing search query: {}", searchQuery.getQuery().toString());
        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class, IndexCoordinates.of("posts"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
