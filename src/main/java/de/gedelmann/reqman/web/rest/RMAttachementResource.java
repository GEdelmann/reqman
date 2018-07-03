package de.gedelmann.reqman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.gedelmann.reqman.service.RMAttachementService;
import de.gedelmann.reqman.web.rest.errors.BadRequestAlertException;
import de.gedelmann.reqman.web.rest.util.HeaderUtil;
import de.gedelmann.reqman.web.rest.util.PaginationUtil;
import de.gedelmann.reqman.service.dto.RMAttachementDTO;
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
 * REST controller for managing RMAttachement.
 */
@RestController
@RequestMapping("/api")
public class RMAttachementResource {

    private final Logger log = LoggerFactory.getLogger(RMAttachementResource.class);

    private static final String ENTITY_NAME = "rMAttachement";

    private final RMAttachementService rMAttachementService;

    public RMAttachementResource(RMAttachementService rMAttachementService) {
        this.rMAttachementService = rMAttachementService;
    }

    /**
     * POST  /rm-attachements : Create a new rMAttachement.
     *
     * @param rMAttachementDTO the rMAttachementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rMAttachementDTO, or with status 400 (Bad Request) if the rMAttachement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rm-attachements")
    @Timed
    public ResponseEntity<RMAttachementDTO> createRMAttachement(@RequestBody RMAttachementDTO rMAttachementDTO) throws URISyntaxException {
        log.debug("REST request to save RMAttachement : {}", rMAttachementDTO);
        if (rMAttachementDTO.getId() != null) {
            throw new BadRequestAlertException("A new rMAttachement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RMAttachementDTO result = rMAttachementService.save(rMAttachementDTO);
        return ResponseEntity.created(new URI("/api/rm-attachements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rm-attachements : Updates an existing rMAttachement.
     *
     * @param rMAttachementDTO the rMAttachementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rMAttachementDTO,
     * or with status 400 (Bad Request) if the rMAttachementDTO is not valid,
     * or with status 500 (Internal Server Error) if the rMAttachementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rm-attachements")
    @Timed
    public ResponseEntity<RMAttachementDTO> updateRMAttachement(@RequestBody RMAttachementDTO rMAttachementDTO) throws URISyntaxException {
        log.debug("REST request to update RMAttachement : {}", rMAttachementDTO);
        if (rMAttachementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RMAttachementDTO result = rMAttachementService.save(rMAttachementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rMAttachementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rm-attachements : get all the rMAttachements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rMAttachements in body
     */
    @GetMapping("/rm-attachements")
    @Timed
    public ResponseEntity<List<RMAttachementDTO>> getAllRMAttachements(Pageable pageable) {
        log.debug("REST request to get a page of RMAttachements");
        Page<RMAttachementDTO> page = rMAttachementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rm-attachements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rm-attachements/:id : get the "id" rMAttachement.
     *
     * @param id the id of the rMAttachementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rMAttachementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rm-attachements/{id}")
    @Timed
    public ResponseEntity<RMAttachementDTO> getRMAttachement(@PathVariable Long id) {
        log.debug("REST request to get RMAttachement : {}", id);
        Optional<RMAttachementDTO> rMAttachementDTO = rMAttachementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMAttachementDTO);
    }

    /**
     * DELETE  /rm-attachements/:id : delete the "id" rMAttachement.
     *
     * @param id the id of the rMAttachementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rm-attachements/{id}")
    @Timed
    public ResponseEntity<Void> deleteRMAttachement(@PathVariable Long id) {
        log.debug("REST request to delete RMAttachement : {}", id);
        rMAttachementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rm-attachements?query=:query : search for the rMAttachement corresponding
     * to the query.
     *
     * @param query the query of the rMAttachement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rm-attachements")
    @Timed
    public ResponseEntity<List<RMAttachementDTO>> searchRMAttachements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RMAttachements for query {}", query);
        Page<RMAttachementDTO> page = rMAttachementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rm-attachements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
