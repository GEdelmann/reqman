package de.gedelmann.reqman.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RMPageSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RMPageSearchRepositoryMockConfiguration {

    @MockBean
    private RMPageSearchRepository mockRMPageSearchRepository;

}
