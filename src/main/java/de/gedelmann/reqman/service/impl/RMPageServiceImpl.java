package de.gedelmann.reqman.service.impl;

import de.gedelmann.reqman.service.RMPageService;
import de.gedelmann.reqman.domain.RMPage;
import de.gedelmann.reqman.repository.RMPageRepository;
import de.gedelmann.reqman.repository.search.RMPageSearchRepository;
import de.gedelmann.reqman.service.dto.RMPageDTO;
import de.gedelmann.reqman.service.mapper.RMPageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RMPage.
 */
@Service
@Transactional
public class RMPageServiceImpl implements RMPageService {

    private final Logger log = LoggerFactory.getLogger(RMPageServiceImpl.class);

    private final RMPageRepository rMPageRepository;

    private final RMPageMapper rMPageMapper;

    private final RMPageSearchRepository rMPageSearchRepository;

    public RMPageServiceImpl(RMPageRepository rMPageRepository, RMPageMapper rMPageMapper, RMPageSearchRepository rMPageSearchRepository) {
        this.rMPageRepository = rMPageRepository;
        this.rMPageMapper = rMPageMapper;
        this.rMPageSearchRepository = rMPageSearchRepository;
    }

    /**
     * Save a rMPage.
     *
     * @param rMPageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RMPageDTO save(RMPageDTO rMPageDTO) {
        log.debug("Request to save RMPage : {}", rMPageDTO);
        RMPage rMPage = rMPageMapper.toEntity(rMPageDTO);
        rMPage = rMPageRepository.save(rMPage);
        RMPageDTO result = rMPageMapper.toDto(rMPage);
        rMPageSearchRepository.save(rMPage);
        return result;
    }

    /**
     * Get all the rMPages.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RMPageDTO> findAll() {
        log.debug("Request to get all RMPages");
        return rMPageRepository.findAll().stream()
            .map(rMPageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one rMPage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RMPageDTO> findOne(Long id) {
        log.debug("Request to get RMPage : {}", id);
        return rMPageRepository.findById(id)
            .map(rMPageMapper::toDto);
    }

    /**
     * Delete the rMPage by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMPage : {}", id);
        rMPageRepository.deleteById(id);
        rMPageSearchRepository.deleteById(id);
    }

    /**
     * Search for the rMPage corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RMPageDTO> search(String query) {
        log.debug("Request to search RMPages for query {}", query);
        return StreamSupport
            .stream(rMPageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rMPageMapper::toDto)
            .collect(Collectors.toList());
    }
}
