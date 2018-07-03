package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.service.RMRequirementService;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.web.rest.util.PaginationUtil;
import de.gedelmann.reqman.service.dto.RMRequirementDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RMRequirement.
 */
@RestController
@RequestMapping("/api")
public class RMRequirementResource {

    private final Logger log = LoggerFactory.getLogger(RMRequirementResource.class);

    private static final String ENTITY_NAME = "rMRequirement";

    private final RMRequirementService rMRequirementService;

    public RMRequirementResource(RMRequirementService rMRequirementService) {
        this.rMRequirementService = rMRequirementService;
    }

    /**
     * POST  /rm-requirements : Create a new rMRequirement.
     *
     * @param rMRequirementDTO the rMRequirementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMRequirementDTO, or with status 400 (Bad Request) if the rMRequirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-requirements")
    @Timed
    public ResponseEntity<RMRequirementDTO> createRMRequirement(@Valid @RequestBody RMRequirementDTO rMRequirementDTO) throws URISyntaxException {
        log.debug("REST request to save RMRequirement : {}", rMRequirementDTO);
        if (rMRequirementDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMRequirement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RMRequirementDTO result = rMRequirementService.save(rMRequirementDTO);
        return ResponseEntity.created(new URI("/api/rm-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-requirements : Updates an existing rMRequirement.
     *
     * @param rMRequirementDTO the rMRequirementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMRequirementDTO,
     * or with status 400 (Bad Request) if the rMRequirementDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMRequirementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-requirements")
    @Timed
    public ResponseEntity<RMRequirementDTO> updateRMRequirement(@Valid @RequestBody RMRequirementDTO rMRequirementDTO) throws URISyntaxException {
        log.debug("REST request to update RMRequirement : {}", rMRequirementDTO);
        if (rMRequirementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RMRequirementDTO result = rMRequirementService.save(rMRequirementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMRequirementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-requirements : get all the rMRequirements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rMRequirements in body
     */
    @GetMapping("/rm-requirements")
    @Timed
    public ResponseEntity<List<RMRequirementDTO>> getAllRMRequirements(Pageable pageable) {
        log.debug("REST request to get a page of RMRequirements");
        Page<RMRequirementDTO> page = rMRequirementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rm-requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rm-requirements/:id : get the "id" rMRequirement.
     *
     * @param id the id of the rMRequirementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMRequirementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-requirements/{id}")
    @Timed
    public ResponseEntity<RMRequirementDTO> getRMRequirement(@PathVariable Long id) {
        log.debug("REST request to get RMRequirement : {}", id);
        Optional<RMRequirementDTO> rMRequirementDTO = rMRequirementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMRequirementDTO);
    }

    /**
     * DELETE  /rm-requirements/:id : delete the "id" rMRequirement.
     *
     * @param id the id of the rMRequirementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-requirements/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMRequirement(@PathVariable Long id) {
        log.debug("REST request to delete RMRequirement : {}", id);
        rMRequirementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-requirements?query=:query : search for the rMRequirement corresponding
     * to the query.
     *
     * @param query the query of the rMRequirement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rm-requirements")
    @Timed
    public ResponseEntity<List<RMRequirementDTO>> searchRMRequirements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RMRequirements for query {}", query);
        Page<RMRequirementDTO> page = rMRequirementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rm-requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
