package de.gedelmann.reqman.service;

import de.gedelmann.reqman.service.dto.RMPageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RMPage.
 */
public interface RMPageService {

    /**
     * Save a rMPage.
     *
     * @param rMPageDTO the entity to save
     * @return the persisted entity
     */
    RMPageDTO save(RMPageDTO rMPageDTO);

    /**
     * Get all the rMPages.
     *
     * @return the list of entities
     */
    List<RMPageDTO> findAll();


    /**
     * Get the "id" rMPage.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RMPageDTO> findOne(Long id);

    /**
     * Delete the "id" rMPage.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rMPage corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RMPageDTO> search(String query);
}
