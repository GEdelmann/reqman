package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMPage entity.
 */
public interface RMPageSearchRepository extends ElasticsearchRepository<RMPage, Long> {
}
