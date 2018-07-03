package de.gedelmann.reqman.service;

import de.gedelmann.reqman.service.dto.RMTagDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RMTag.
 */
public interface RMTagService {

    /**
     * Save a rMTag.
     *
     * @param rMTagDTO the entity to save
     * @return the persisted entity
     */
    RMTagDTO save(RMTagDTO rMTagDTO);

    /**
     * Get all the rMTags.
     *
     * @return the list of entities
     */
    List<RMTagDTO> findAll();

    /**
     * Get all the RMTag with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<RMTagDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" rMTag.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RMTagDTO> findOne(Long id);

    /**
     * Delete the "id" rMTag.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rMTag corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RMTagDTO> search(String query);
}
