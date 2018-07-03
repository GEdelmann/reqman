package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.service.RMTagService;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.service.dto.RMTagDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RMTag.
 */
@RestController
@RequestMapping("/api")
public class RMTagResource {

    private final Logger log = LoggerFactory.getLogger(RMTagResource.class);

    private static final String ENTITY_NAME = "rMTag";

    private final RMTagService rMTagService;

    public RMTagResource(RMTagService rMTagService) {
        this.rMTagService = rMTagService;
    }

    /**
     * POST  /rm-tags : Create a new rMTag.
     *
     * @param rMTagDTO the rMTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMTagDTO, or with status 400 (Bad Request) if the rMTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-tags")
    @Timed
    public ResponseEntity<RMTagDTO> createRMTag(@RequestBody RMTagDTO rMTagDTO) throws URISyntaxException {
        log.debug("REST request to save RMTag : {}", rMTagDTO);
        if (rMTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RMTagDTO result = rMTagService.save(rMTagDTO);
        return ResponseEntity.created(new URI("/api/rm-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-tags : Updates an existing rMTag.
     *
     * @param rMTagDTO the rMTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMTagDTO,
     * or with status 400 (Bad Request) if the rMTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMTagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-tags")
    @Timed
    public ResponseEntity<RMTagDTO> updateRMTag(@RequestBody RMTagDTO rMTagDTO) throws URISyntaxException {
        log.debug("REST request to update RMTag : {}", rMTagDTO);
        if (rMTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RMTagDTO result = rMTagService.save(rMTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-tags : get all the rMTags.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of rMTags in body
     */
    @GetMapping("/rm-tags")
    @Timed
    public List<RMTagDTO> getAllRMTags(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all RMTags");
        return rMTagService.findAll();
    }

    /**
     * GET  /rm-tags/:id : get the "id" rMTag.
     *
     * @param id the id of the rMTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMTagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-tags/{id}")
    @Timed
    public ResponseEntity<RMTagDTO> getRMTag(@PathVariable Long id) {
        log.debug("REST request to get RMTag : {}", id);
        Optional<RMTagDTO> rMTagDTO = rMTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMTagDTO);
    }

    /**
     * DELETE  /rm-tags/:id : delete the "id" rMTag.
     *
     * @param id the id of the rMTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMTag(@PathVariable Long id) {
        log.debug("REST request to delete RMTag : {}", id);
        rMTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-tags?query=:query : search for the rMTag corresponding
     * to the query.
     *
     * @param query the query of the rMTag search
     * @return the result of the search
     */
    @GetMapping("/_search/rm-tags")
    @Timed
    public List<RMTagDTO> searchRMTags(@RequestParam String query) {
        log.debug("REST request to search RMTags for query {}", query);
        return rMTagService.search(query);
    }

}
