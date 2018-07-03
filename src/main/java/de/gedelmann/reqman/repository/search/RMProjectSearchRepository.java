package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMProject entity.
 */
public interface RMProjectSearchRepository extends ElasticsearchRepository<RMProject, Long> {
}
