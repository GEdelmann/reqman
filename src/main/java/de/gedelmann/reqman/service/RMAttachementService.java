package de.gedelmann.reqman.service;

import de.gedelmann.reqman.service.dto.RMAttachementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RMAttachement.
 */
public interface RMAttachementService {

    /**
     * Save a rMAttachement.
     *
     * @param rMAttachementDTO the entity to save
     * @return the persisted entity
     */
    RMAttachementDTO save(RMAttachementDTO rMAttachementDTO);

    /**
     * Get all the rMAttachements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMAttachementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rMAttachement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RMAttachementDTO> findOne(Long id);

    /**
     * Delete the "id" rMAttachement.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rMAttachement corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMAttachementDTO> search(String query, Pageable pageable);
}
