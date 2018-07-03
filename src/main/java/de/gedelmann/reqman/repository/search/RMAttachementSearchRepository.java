package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMAttachement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMAttachement entity.
 */
public interface RMAttachementSearchRepository extends ElasticsearchRepository<RMAttachement, Long> {
}
