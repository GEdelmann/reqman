package de.gedelmann.reqman.service.impl;

import de.gedelmann.reqman.service.RMRequirementService;
import de.gedelmann.reqman.domain.RMRequirement;
import de.gedelmann.reqman.repository.RMRequirementRepository;
import de.gedelmann.reqman.repository.search.RMRequirementSearchRepository;
import de.gedelmann.reqman.service.dto.RMRequirementDTO;
import de.gedelmann.reqman.service.mapper.RMRequirementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RMRequirement.
 */
@Service
@Transactional
public class RMRequirementServiceImpl implements RMRequirementService {

    private final Logger log = LoggerFactory.getLogger(RMRequirementServiceImpl.class);

    private final RMRequirementRepository rMRequirementRepository;

    private final RMRequirementMapper rMRequirementMapper;

    private final RMRequirementSearchRepository rMRequirementSearchRepository;

    public RMRequirementServiceImpl(RMRequirementRepository rMRequirementRepository, RMRequirementMapper rMRequirementMapper, RMRequirementSearchRepository rMRequirementSearchRepository) {
        this.rMRequirementRepository = rMRequirementRepository;
        this.rMRequirementMapper = rMRequirementMapper;
        this.rMRequirementSearchRepository = rMRequirementSearchRepository;
    }

    /**
     * Save a rMRequirement.
     *
     * @param rMRequirementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RMRequirementDTO save(RMRequirementDTO rMRequirementDTO) {
        log.debug("Request to save RMRequirement : {}", rMRequirementDTO);
        RMRequirement rMRequirement = rMRequirementMapper.toEntity(rMRequirementDTO);
        rMRequirement = rMRequirementRepository.save(rMRequirement);
        RMRequirementDTO result = rMRequirementMapper.toDto(rMRequirement);
        rMRequirementSearchRepository.save(rMRequirement);
        return result;
    }

    /**
     * Get all the rMRequirements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMRequirementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RMRequirements");
        return rMRequirementRepository.findAll(pageable)
            .map(rMRequirementMapper::toDto);
    }


    /**
     * Get one rMRequirement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RMRequirementDTO> findOne(Long id) {
        log.debug("Request to get RMRequirement : {}", id);
        return rMRequirementRepository.findById(id)
            .map(rMRequirementMapper::toDto);
    }

    /**
     * Delete the rMRequirement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMRequirement : {}", id);
        rMRequirementRepository.deleteById(id);
        rMRequirementSearchRepository.deleteById(id);
    }

    /**
     * Search for the rMRequirement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMRequirementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RMRequirements for query {}", query);
        return rMRequirementSearchRepository.search(queryStringQuery(query), pageable)
            .map(rMRequirementMapper::toDto);
    }
}
