package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.service.RMProjectService;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.web.rest.util.PaginationUtil;
import de.gedelmann.reqman.service.dto.RMProjectDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RMProject.
 */
@RestController
@RequestMapping("/api")
public class RMProjectResource {

    private final Logger log = LoggerFactory.getLogger(RMProjectResource.class);

    private static final String ENTITY_NAME = "rMProject";

    private final RMProjectService rMProjectService;

    public RMProjectResource(RMProjectService rMProjectService) {
        this.rMProjectService = rMProjectService;
    }

    /**
     * POST  /rm-projects : Create a new rMProject.
     *
     * @param rMProjectDTO the rMProjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMProjectDTO, or with status 400 (Bad Request) if the rMProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-projects")
    @Timed
    public ResponseEntity<RMProjectDTO> createRMProject(@RequestBody RMProjectDTO rMProjectDTO) throws URISyntaxException {
        log.debug("REST request to save RMProject : {}", rMProjectDTO);
        if (rMProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RMProjectDTO result = rMProjectService.save(rMProjectDTO);
        return ResponseEntity.created(new URI("/api/rm-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-projects : Updates an existing rMProject.
     *
     * @param rMProjectDTO the rMProjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMProjectDTO,
     * or with status 400 (Bad Request) if the rMProjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMProjectDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-projects")
    @Timed
    public ResponseEntity<RMProjectDTO> updateRMProject(@RequestBody RMProjectDTO rMProjectDTO) throws URISyntaxException {
        log.debug("REST request to update RMProject : {}", rMProjectDTO);
        if (rMProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RMProjectDTO result = rMProjectService.save(rMProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMProjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-projects : get all the rMProjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rMProjects in body
     */
    @GetMapping("/rm-projects")
    @Timed
    public ResponseEntity<List<RMProjectDTO>> getAllRMProjects(Pageable pageable) {
        log.debug("REST request to get a page of RMProjects");
        Page<RMProjectDTO> page = rMProjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rm-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rm-projects/:id : get the "id" rMProject.
     *
     * @param id the id of the rMProjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMProjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-projects/{id}")
    @Timed
    public ResponseEntity<RMProjectDTO> getRMProject(@PathVariable Long id) {
        log.debug("REST request to get RMProject : {}", id);
        Optional<RMProjectDTO> rMProjectDTO = rMProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMProjectDTO);
    }

    /**
     * DELETE  /rm-projects/:id : delete the "id" rMProject.
     *
     * @param id the id of the rMProjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMProject(@PathVariable Long id) {
        log.debug("REST request to delete RMProject : {}", id);
        rMProjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-projects?query=:query : search for the rMProject corresponding
     * to the query.
     *
     * @param query the query of the rMProject search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rm-projects")
    @Timed
    public ResponseEntity<List<RMProjectDTO>> searchRMProjects(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RMProjects for query {}", query);
        Page<RMProjectDTO> page = rMProjectService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rm-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
