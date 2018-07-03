package de.gedelmann.reqman.service;

import de.gedelmann.reqman.service.dto.RMRequirementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RMRequirement.
 */
public interface RMRequirementService {

    /**
     * Save a rMRequirement.
     *
     * @param rMRequirementDTO the entity to save
     * @return the persisted entity
     */
    RMRequirementDTO save(RMRequirementDTO rMRequirementDTO);

    /**
     * Get all the rMRequirements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMRequirementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rMRequirement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RMRequirementDTO> findOne(Long id);

    /**
     * Delete the "id" rMRequirement.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rMRequirement corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RMRequirementDTO> search(String query, Pageable pageable);
}
