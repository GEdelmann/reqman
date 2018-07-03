package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.service.RMPageService;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.service.dto.RMPageDTO;
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
 * REST controller for managing RMPage.
 */
@RestController
@RequestMapping("/api")
public class RMPageResource {

    private final Logger log = LoggerFactory.getLogger(RMPageResource.class);

    private static final String ENTITY_NAME = "rMPage";

    private final RMPageService rMPageService;

    public RMPageResource(RMPageService rMPageService) {
        this.rMPageService = rMPageService;
    }

    /**
     * POST  /rm-pages : Create a new rMPage.
     *
     * @param rMPageDTO the rMPageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMPageDTO, or with status 400 (Bad Request) if the rMPage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-pages")
    @Timed
    public ResponseEntity<RMPageDTO> createRMPage(@RequestBody RMPageDTO rMPageDTO) throws URISyntaxException {
        log.debug("REST request to save RMPage : {}", rMPageDTO);
        if (rMPageDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RMPageDTO result = rMPageService.save(rMPageDTO);
        return ResponseEntity.created(new URI("/api/rm-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-pages : Updates an existing rMPage.
     *
     * @param rMPageDTO the rMPageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMPageDTO,
     * or with status 400 (Bad Request) if the rMPageDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMPageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-pages")
    @Timed
    public ResponseEntity<RMPageDTO> updateRMPage(@RequestBody RMPageDTO rMPageDTO) throws URISyntaxException {
        log.debug("REST request to update RMPage : {}", rMPageDTO);
        if (rMPageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RMPageDTO result = rMPageService.save(rMPageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMPageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-pages : get all the rMPages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rMPages in body
     */
    @GetMapping("/rm-pages")
    @Timed
    public List<RMPageDTO> getAllRMPages() {
        log.debug("REST request to get all RMPages");
        return rMPageService.findAll();
    }

    /**
     * GET  /rm-pages/:id : get the "id" rMPage.
     *
     * @param id the id of the rMPageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMPageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-pages/{id}")
    @Timed
    public ResponseEntity<RMPageDTO> getRMPage(@PathVariable Long id) {
        log.debug("REST request to get RMPage : {}", id);
        Optional<RMPageDTO> rMPageDTO = rMPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMPageDTO);
    }

    /**
     * DELETE  /rm-pages/:id : delete the "id" rMPage.
     *
     * @param id the id of the rMPageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-pages/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMPage(@PathVariable Long id) {
        log.debug("REST request to delete RMPage : {}", id);
        rMPageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-pages?query=:query : search for the rMPage corresponding
     * to the query.
     *
     * @param query the query of the rMPage search
     * @return the result of the search
     */
    @GetMapping("/_search/rm-pages")
    @Timed
    public List<RMPageDTO> searchRMPages(@RequestParam String query) {
        log.debug("REST request to search RMPages for query {}", query);
        return rMPageService.search(query);
    }

}
