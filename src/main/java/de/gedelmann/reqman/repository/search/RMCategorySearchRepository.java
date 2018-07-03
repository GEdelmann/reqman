package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMCategory entity.
 */
public interface RMCategorySearchRepository extends ElasticsearchRepository<RMCategory, Long> {
}
