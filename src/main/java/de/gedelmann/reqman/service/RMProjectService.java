package de.gedelmann.reqman.service;

import de.gedelmann.reqman.service.dto.RMProjectDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RMProject.
 */
public interface RMProjectService {

    /**
     * Save a rMProject.
     *
     * @param rMProjectDTO the entity to save
     * @return the persisted entity
     */
    RMProjectDTO save(RMProjectDTO rMProjectDTO);

    /**
     * Get all the rMProjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMProjectDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rMProject.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RMProjectDTO> findOne(Long id);

    /**
     * Delete the "id" rMProject.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rMProject corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMProjectDTO> search(String query, Pageable pageable);
}
