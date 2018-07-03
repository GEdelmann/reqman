package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMAttachement;
import de.gedelmann.reqman.repository.RMAttachementRepository;
import de.gedelmann.reqman.repository.search.RMAttachementSearchRepository;
import de.gedelmann.reqman.service.RMAttachementService;
import de.gedelmann.reqman.service.dto.RMAttachementDTO;
import de.gedelmann.reqman.service.mapper.RMAttachementMapper;
import de.gedelmann.reqman.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static de.gedelmann.reqman.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RMAttachementResource REST controller.
 *
 * @see RMAttachementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMAttachementResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private RMAttachementRepository rMAttachementRepository;


    @Autowired
    private RMAttachementMapper rMAttachementMapper;
    

    @Autowired
    private RMAttachementService rMAttachementService;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMAttachementSearchRepositoryMockConfiguration
     */
    @Autowired
    private RMAttachementSearchRepository mockRMAttachementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMAttachementMockMvc;

    private RMAttachement rMAttachement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMAttachementResource rMAttachementResource = new RMAttachementResource(rMAttachementService);
        this.restRMAttachementMockMvc = MockMvcBuilders.standaloneSetup(rMAttachementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RMAttachement createEntity(EntityManager em) {
        RMAttachement rMAttachement = new RMAttachement()
            .name(DEFAULT_NAME)
            .mimeType(DEFAULT_MIME_TYPE)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return rMAttachement;
    }

    @Before
    public void initTest() {
        rMAttachement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMAttachement() throws Exception {
        int databaseSizeBeforeCreate = rMAttachementRepository.findAll().size();

        // Create the RMAttachement
        RMAttachementDTO rMAttachementDTO = rMAttachementMapper.toDto(rMAttachement);
        restRMAttachementMockMvc.perform(post("/api/rm-attachements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMAttachementDTO)))
            .andExpect(status().isCreated());

        // Validate the RMAttachement in the database
        List<RMAttachement> rMAttachementList = rMAttachementRepository.findAll();
        assertThat(rMAttachementList).hasSize(databaseSizeBeforeCreate + 1);
        RMAttachement testRMAttachement = rMAttachementList.get(rMAttachementList.size() - 1);
        assertThat(testRMAttachement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRMAttachement.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testRMAttachement.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRMAttachement.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);

        // Validate the RMAttachement in Elasticsearch
        verify(mockRMAttachementSearchRepository, times(1)).save(testRMAttachement);
    }

    @Test
    @Transactional
    public void createRMAttachementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMAttachementRepository.findAll().size();

        // Create the RMAttachement with an existing ID
        rMAttachement.setId(1L);
        RMAttachementDTO rMAttachementDTO = rMAttachementMapper.toDto(rMAttachement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMAttachementMockMvc.perform(post("/api/rm-attachements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMAttachementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMAttachement in the database
        List<RMAttachement> rMAttachementList = rMAttachementRepository.findAll();
        assertThat(rMAttachementList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMAttachement in Elasticsearch
        verify(mockRMAttachementSearchRepository, times(0)).save(rMAttachement);
    }

    @Test
    @Transactional
    public void getAllRMAttachements() throws Exception {
        // Initialize the database
        rMAttachementRepository.saveAndFlush(rMAttachement);

        // Get all the rMAttachementList
        restRMAttachementMockMvc.perform(get("/api/rm-attachements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMAttachement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }
    

    @Test
    @Transactional
    public void getRMAttachement() throws Exception {
        // Initialize the database
        rMAttachementRepository.saveAndFlush(rMAttachement);

        // Get the rMAttachement
        restRMAttachementMockMvc.perform(get("/api/rm-attachements/{id}", rMAttachement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMAttachement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }
    @Test
    @Transactional
    public void getNonExistingRMAttachement() throws Exception {
        // Get the rMAttachement
        restRMAttachementMockMvc.perform(get("/api/rm-attachements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMAttachement() throws Exception {
        // Initialize the database
        rMAttachementRepository.saveAndFlush(rMAttachement);

        int databaseSizeBeforeUpdate = rMAttachementRepository.findAll().size();

        // Update the rMAttachement
        RMAttachement updatedRMAttachement = rMAttachementRepository.findById(rMAttachement.getId()).get();
        // Disconnect from session so that the updates on updatedRMAttachement are not directly saved in db
        em.detach(updatedRMAttachement);
        updatedRMAttachement
            .name(UPDATED_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);
        RMAttachementDTO rMAttachementDTO = rMAttachementMapper.toDto(updatedRMAttachement);

        restRMAttachementMockMvc.perform(put("/api/rm-attachements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMAttachementDTO)))
            .andExpect(status().isOk());

        // Validate the RMAttachement in the database
        List<RMAttachement> rMAttachementList = rMAttachementRepository.findAll();
        assertThat(rMAttachementList).hasSize(databaseSizeBeforeUpdate);
        RMAttachement testRMAttachement = rMAttachementList.get(rMAttachementList.size() - 1);
        assertThat(testRMAttachement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRMAttachement.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testRMAttachement.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRMAttachement.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);

        // Validate the RMAttachement in Elasticsearch
        verify(mockRMAttachementSearchRepository, times(1)).save(testRMAttachement);
    }

    @Test
    @Transactional
    public void updateNonExistingRMAttachement() throws Exception {
        int databaseSizeBeforeUpdate = rMAttachementRepository.findAll().size();

        // Create the RMAttachement
        RMAttachementDTO rMAttachementDTO = rMAttachementMapper.toDto(rMAttachement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMAttachementMockMvc.perform(put("/api/rm-attachements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMAttachementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMAttachement in the database
        List<RMAttachement> rMAttachementList = rMAttachementRepository.findAll();
        assertThat(rMAttachementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMAttachement in Elasticsearch
        verify(mockRMAttachementSearchRepository, times(0)).save(rMAttachement);
    }

    @Test
    @Transactional
    public void deleteRMAttachement() throws Exception {
        // Initialize the database
        rMAttachementRepository.saveAndFlush(rMAttachement);

        int databaseSizeBeforeDelete = rMAttachementRepository.findAll().size();

        // Get the rMAttachement
        restRMAttachementMockMvc.perform(delete("/api/rm-attachements/{id}", rMAttachement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMAttachement> rMAttachementList = rMAttachementRepository.findAll();
        assertThat(rMAttachementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMAttachement in Elasticsearch
        verify(mockRMAttachementSearchRepository, times(1)).deleteById(rMAttachement.getId());
    }

    @Test
    @Transactional
    public void searchRMAttachement() throws Exception {
        // Initialize the database
        rMAttachementRepository.saveAndFlush(rMAttachement);
        when(mockRMAttachementSearchRepository.search(queryStringQuery("id:" + rMAttachement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rMAttachement), PageRequest.of(0, 1), 1));
        // Search the rMAttachement
        restRMAttachementMockMvc.perform(get("/api/_search/rm-attachements?query=id:" + rMAttachement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMAttachement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMAttachement.class);
        RMAttachement rMAttachement1 = new RMAttachement();
        rMAttachement1.setId(1L);
        RMAttachement rMAttachement2 = new RMAttachement();
        rMAttachement2.setId(rMAttachement1.getId());
        assertThat(rMAttachement1).isEqualTo(rMAttachement2);
        rMAttachement2.setId(2L);
        assertThat(rMAttachement1).isNotEqualTo(rMAttachement2);
        rMAttachement1.setId(null);
        assertThat(rMAttachement1).isNotEqualTo(rMAttachement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMAttachementDTO.class);
        RMAttachementDTO rMAttachementDTO1 = new RMAttachementDTO();
        rMAttachementDTO1.setId(1L);
        RMAttachementDTO rMAttachementDTO2 = new RMAttachementDTO();
        assertThat(rMAttachementDTO1).isNotEqualTo(rMAttachementDTO2);
        rMAttachementDTO2.setId(rMAttachementDTO1.getId());
        assertThat(rMAttachementDTO1).isEqualTo(rMAttachementDTO2);
        rMAttachementDTO2.setId(2L);
        assertThat(rMAttachementDTO1).isNotEqualTo(rMAttachementDTO2);
        rMAttachementDTO1.setId(null);
        assertThat(rMAttachementDTO1).isNotEqualTo(rMAttachementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMAttachementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMAttachementMapper.fromId(null)).isNull();
    }
}
