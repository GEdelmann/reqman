package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMTag entity.
 */
public interface RMTagSearchRepository extends ElasticsearchRepository<RMTag, Long> {
}
