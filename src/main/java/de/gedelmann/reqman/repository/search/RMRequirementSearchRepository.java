package de.gedelmann.reqman.repository.search;

import de.gedelmann.reqman.domain.RMRequirement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RMRequirement entity.
 */
public interface RMRequirementSearchRepository extends ElasticsearchRepository<RMRequirement, Long> {
}
