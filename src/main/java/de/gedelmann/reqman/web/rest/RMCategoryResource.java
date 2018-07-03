package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.domain.RMCategory;
import de.gedelmann.reqman.repository.RMCategoryRepository;
import de.gedelmann.reqman.repository.search.RMCategorySearchRepository;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.service.dto.RMCategoryDTO;
import de.gedelmann.reqman.service.mapper.RMCategoryMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RMCategory.
 */
@RestController
@RequestMapping("/api")
public class RMCategoryResource {

    private final Logger log = LoggerFactory.getLogger(RMCategoryResource.class);

    private static final String ENTITY_NAME = "rMCategory";

    private final RMCategoryRepository rMCategoryRepository;

    private final RMCategoryMapper rMCategoryMapper;

    private final RMCategorySearchRepository rMCategorySearchRepository;

    public RMCategoryResource(RMCategoryRepository rMCategoryRepository, RMCategoryMapper rMCategoryMapper, RMCategorySearchRepository rMCategorySearchRepository) {
        this.rMCategoryRepository = rMCategoryRepository;
        this.rMCategoryMapper = rMCategoryMapper;
        this.rMCategorySearchRepository = rMCategorySearchRepository;
    }

    /**
     * POST  /rm-categories : Create a new rMCategory.
     *
     * @param rMCategoryDTO the rMCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMCategoryDTO, or with status 400 (Bad Request) if the rMCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-categories")
    @Timed
    public ResponseEntity<RMCategoryDTO> createRMCategory(@RequestBody RMCategoryDTO rMCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save RMCategory : {}", rMCategoryDTO);
        if (rMCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }

        RMCategory rMCategory = rMCategoryMapper.toEntity(rMCategoryDTO);
        rMCategory = rMCategoryRepository.save(rMCategory);
        RMCategoryDTO result = rMCategoryMapper.toDto(rMCategory);
        rMCategorySearchRepository.save(rMCategory);
        return ResponseEntity.created(new URI("/api/rm-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-categories : Updates an existing rMCategory.
     *
     * @param rMCategoryDTO the rMCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMCategoryDTO,
     * or with status 400 (Bad Request) if the rMCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-categories")
    @Timed
    public ResponseEntity<RMCategoryDTO> updateRMCategory(@RequestBody RMCategoryDTO rMCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update RMCategory : {}", rMCategoryDTO);
        if (rMCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        RMCategory rMCategory = rMCategoryMapper.toEntity(rMCategoryDTO);
        rMCategory = rMCategoryRepository.save(rMCategory);
        RMCategoryDTO result = rMCategoryMapper.toDto(rMCategory);
        rMCategorySearchRepository.save(rMCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-categories : get all the rMCategories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of rMCategories in body
     */
    @GetMapping("/rm-categories")
    @Timed
    public List<RMCategoryDTO> getAllRMCategories(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all RMCategories");
        List<RMCategory> rMCategories = rMCategoryRepository.findAllWithEagerRelationships();
        return rMCategoryMapper.toDto(rMCategories);
    }

    /**
     * GET  /rm-categories/:id : get the "id" rMCategory.
     *
     * @param id the id of the rMCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-categories/{id}")
    @Timed
    public ResponseEntity<RMCategoryDTO> getRMCategory(@PathVariable Long id) {
        log.debug("REST request to get RMCategory : {}", id);
        Optional<RMCategoryDTO> rMCategoryDTO = rMCategoryRepository.findOneWithEagerRelationships(id)
            .map(rMCategoryMapper::toDto);
        return ResponseUtil.wrapOrNotFound(rMCategoryDTO);
    }

    /**
     * DELETE  /rm-categories/:id : delete the "id" rMCategory.
     *
     * @param id the id of the rMCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMCategory(@PathVariable Long id) {
        log.debug("REST request to delete RMCategory : {}", id);

        rMCategoryRepository.deleteById(id);
        rMCategorySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-categories?query=:query : search for the rMCategory corresponding
     * to the query.
     *
     * @param query the query of the rMCategory search
     * @return the result of the search
     */
    @GetMapping("/_search/rm-categories")
    @Timed
    public List<RMCategoryDTO> searchRMCategories(@RequestParam String query) {
        log.debug("REST request to search RMCategories for query {}", query);
        return StreamSupport
            .stream(rMCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rMCategoryMapper::toDto)
            .collect(Collectors.toList());
    }

}
