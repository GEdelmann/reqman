package de.gedelmann.reqman.service.impl;

import de.gedelmann.reqman.service.RMTagService;
import de.gedelmann.reqman.domain.RMTag;
import de.gedelmann.reqman.repository.RMTagRepository;
import de.gedelmann.reqman.repository.search.RMTagSearchRepository;
import de.gedelmann.reqman.service.dto.RMTagDTO;
import de.gedelmann.reqman.service.mapper.RMTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RMTag.
 */
@Service
@Transactional
public class RMTagServiceImpl implements RMTagService {

    private final Logger log = LoggerFactory.getLogger(RMTagServiceImpl.class);

    private final RMTagRepository rMTagRepository;

    private final RMTagMapper rMTagMapper;

    private final RMTagSearchRepository rMTagSearchRepository;

    public RMTagServiceImpl(RMTagRepository rMTagRepository, RMTagMapper rMTagMapper, RMTagSearchRepository rMTagSearchRepository) {
        this.rMTagRepository = rMTagRepository;
        this.rMTagMapper = rMTagMapper;
        this.rMTagSearchRepository = rMTagSearchRepository;
    }

    /**
     * Save a rMTag.
     *
     * @param rMTagDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RMTagDTO save(RMTagDTO rMTagDTO) {
        log.debug("Request to save RMTag : {}", rMTagDTO);
        RMTag rMTag = rMTagMapper.toEntity(rMTagDTO);
        rMTag = rMTagRepository.save(rMTag);
        RMTagDTO result = rMTagMapper.toDto(rMTag);
        rMTagSearchRepository.save(rMTag);
        return result;
    }

    /**
     * Get all the rMTags.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RMTagDTO> findAll() {
        log.debug("Request to get all RMTags");
        return rMTagRepository.findAllWithEagerRelationships().stream()
            .map(rMTagMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the RMTag with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<RMTagDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rMTagRepository.findAllWithEagerRelationships(pageable).map(rMTagMapper::toDto);
    }
    

    /**
     * Get one rMTag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RMTagDTO> findOne(Long id) {
        log.debug("Request to get RMTag : {}", id);
        return rMTagRepository.findOneWithEagerRelationships(id)
            .map(rMTagMapper::toDto);
    }

    /**
     * Delete the rMTag by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMTag : {}", id);
        rMTagRepository.deleteById(id);
        rMTagSearchRepository.deleteById(id);
    }

    /**
     * Search for the rMTag corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RMTagDTO> search(String query) {
        log.debug("Request to search RMTags for query {}", query);
        return StreamSupport
            .stream(rMTagSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rMTagMapper::toDto)
            .collect(Collectors.toList());
    }
}
