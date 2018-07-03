package de.gedelmann.reqman.service.impl;

import de.gedelmann.reqman.service.RMProjectService;
import de.gedelmann.reqman.domain.RMProject;
import de.gedelmann.reqman.repository.RMProjectRepository;
import de.gedelmann.reqman.repository.search.RMProjectSearchRepository;
import de.gedelmann.reqman.service.dto.RMProjectDTO;
import de.gedelmann.reqman.service.mapper.RMProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RMProject.
 */
@Service
@Transactional
public class RMProjectServiceImpl implements RMProjectService {

    private final Logger log = LoggerFactory.getLogger(RMProjectServiceImpl.class);

    private final RMProjectRepository rMProjectRepository;

    private final RMProjectMapper rMProjectMapper;

    private final RMProjectSearchRepository rMProjectSearchRepository;

    public RMProjectServiceImpl(RMProjectRepository rMProjectRepository, RMProjectMapper rMProjectMapper, RMProjectSearchRepository rMProjectSearchRepository) {
        this.rMProjectRepository = rMProjectRepository;
        this.rMProjectMapper = rMProjectMapper;
        this.rMProjectSearchRepository = rMProjectSearchRepository;
    }

    /**
     * Save a rMProject.
     *
     * @param rMProjectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RMProjectDTO save(RMProjectDTO rMProjectDTO) {
        log.debug("Request to save RMProject : {}", rMProjectDTO);
        RMProject rMProject = rMProjectMapper.toEntity(rMProjectDTO);
        rMProject = rMProjectRepository.save(rMProject);
        RMProjectDTO result = rMProjectMapper.toDto(rMProject);
        rMProjectSearchRepository.save(rMProject);
        return result;
    }

    /**
     * Get all the rMProjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RMProjects");
        return rMProjectRepository.findAll(pageable)
            .map(rMProjectMapper::toDto);
    }


    /**
     * Get one rMProject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RMProjectDTO> findOne(Long id) {
        log.debug("Request to get RMProject : {}", id);
        return rMProjectRepository.findById(id)
            .map(rMProjectMapper::toDto);
    }

    /**
     * Delete the rMProject by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMProject : {}", id);
        rMProjectRepository.deleteById(id);
        rMProjectSearchRepository.deleteById(id);
    }

    /**
     * Search for the rMProject corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMProjectDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RMProjects for query {}", query);
        return rMProjectSearchRepository.search(queryStringQuery(query), pageable)
            .map(rMProjectMapper::toDto);
    }
}
