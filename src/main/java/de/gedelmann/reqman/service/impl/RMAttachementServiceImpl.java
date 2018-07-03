package de.gedelmann.reqman.service.impl;

import de.gedelmann.reqman.service.RMAttachementService;
import de.gedelmann.reqman.domain.RMAttachement;
import de.gedelmann.reqman.repository.RMAttachementRepository;
import de.gedelmann.reqman.repository.search.RMAttachementSearchRepository;
import de.gedelmann.reqman.service.dto.RMAttachementDTO;
import de.gedelmann.reqman.service.mapper.RMAttachementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RMAttachement.
 */
@Service
@Transactional
public class RMAttachementServiceImpl implements RMAttachementService {

    private final Logger log = LoggerFactory.getLogger(RMAttachementServiceImpl.class);

    private final RMAttachementRepository rMAttachementRepository;

    private final RMAttachementMapper rMAttachementMapper;

    private final RMAttachementSearchRepository rMAttachementSearchRepository;

    public RMAttachementServiceImpl(RMAttachementRepository rMAttachementRepository, RMAttachementMapper rMAttachementMapper, RMAttachementSearchRepository rMAttachementSearchRepository) {
        this.rMAttachementRepository = rMAttachementRepository;
        this.rMAttachementMapper = rMAttachementMapper;
        this.rMAttachementSearchRepository = rMAttachementSearchRepository;
    }

    /**
     * Save a rMAttachement.
     *
     * @param rMAttachementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RMAttachementDTO save(RMAttachementDTO rMAttachementDTO) {
        log.debug("Request to save RMAttachement : {}", rMAttachementDTO);
        RMAttachement rMAttachement = rMAttachementMapper.toEntity(rMAttachementDTO);
        rMAttachement = rMAttachementRepository.save(rMAttachement);
        RMAttachementDTO result = rMAttachementMapper.toDto(rMAttachement);
        rMAttachementSearchRepository.save(rMAttachement);
        return result;
    }

    /**
     * Get all the rMAttachements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMAttachementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RMAttachements");
        return rMAttachementRepository.findAll(pageable)
            .map(rMAttachementMapper::toDto);
    }


    /**
     * Get one rMAttachement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RMAttachementDTO> findOne(Long id) {
        log.debug("Request to get RMAttachement : {}", id);
        return rMAttachementRepository.findById(id)
            .map(rMAttachementMapper::toDto);
    }

    /**
     * Delete the rMAttachement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMAttachement : {}", id);
        rMAttachementRepository.deleteById(id);
        rMAttachementSearchRepository.deleteById(id);
    }

    /**
     * Search for the rMAttachement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RMAttachementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RMAttachements for query {}", query);
        return rMAttachementSearchRepository.search(queryStringQuery(query), pageable)
            .map(rMAttachementMapper::toDto);
    }
}
