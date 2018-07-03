package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMRequirement;
import de.gedelmann.reqman.repository.RMRequirementRepository;
import de.gedelmann.reqman.repository.search.RMRequirementSearchRepository;
import de.gedelmann.reqman.service.RMRequirementService;
import de.gedelmann.reqman.service.dto.RMRequirementDTO;
import de.gedelmann.reqman.service.mapper.RMRequirementMapper;
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

import de.gedelmann.reqman.domain.enumeration.RMRequirementScope;
import de.gedelmann.reqman.domain.enumeration.RMRequirementType;
/**
 * Test class for the RMRequirementResource REST controller.
 *
 * @see RMRequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMRequirementResourceIntTest {

    private static final String DEFAULT_FUNCTIONAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTIONAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HEADLINE = "AAAAAAAAAA";
    private static final String UPDATED_HEADLINE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_GENERAL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_GENERAL_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_NOTES = "BBBBBBBBBB";

    private static final RMRequirementScope DEFAULT_SCOPE = RMRequirementScope.REQUIRED;
    private static final RMRequirementScope UPDATED_SCOPE = RMRequirementScope.NICETOHAVE;

    private static final RMRequirementType DEFAULT_TYPE = RMRequirementType.BUSINESS;
    private static final RMRequirementType UPDATED_TYPE = RMRequirementType.TECHNICAL;

    @Autowired
    private RMRequirementRepository rMRequirementRepository;


    @Autowired
    private RMRequirementMapper rMRequirementMapper;
    

    @Autowired
    private RMRequirementService rMRequirementService;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMRequirementSearchRepositoryMockConfiguration
     */
    @Autowired
    private RMRequirementSearchRepository mockRMRequirementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMRequirementMockMvc;

    private RMRequirement rMRequirement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMRequirementResource rMRequirementResource = new RMRequirementResource(rMRequirementService);
        this.restRMRequirementMockMvc = MockMvcBuilders.standaloneSetup(rMRequirementResource)
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
    public static RMRequirement createEntity(EntityManager em) {
        RMRequirement rMRequirement = new RMRequirement()
            .functionalID(DEFAULT_FUNCTIONAL_ID)
            .headline(DEFAULT_HEADLINE)
            .description(DEFAULT_DESCRIPTION)
            .generalNote(DEFAULT_GENERAL_NOTE)
            .technicalNotes(DEFAULT_TECHNICAL_NOTES)
            .scope(DEFAULT_SCOPE)
            .type(DEFAULT_TYPE);
        return rMRequirement;
    }

    @Before
    public void initTest() {
        rMRequirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMRequirement() throws Exception {
        int databaseSizeBeforeCreate = rMRequirementRepository.findAll().size();

        // Create the RMRequirement
        RMRequirementDTO rMRequirementDTO = rMRequirementMapper.toDto(rMRequirement);
        restRMRequirementMockMvc.perform(post("/api/rm-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMRequirementDTO)))
            .andExpect(status().isCreated());

        // Validate the RMRequirement in the database
        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeCreate + 1);
        RMRequirement testRMRequirement = rMRequirementList.get(rMRequirementList.size() - 1);
        assertThat(testRMRequirement.getFunctionalID()).isEqualTo(DEFAULT_FUNCTIONAL_ID);
        assertThat(testRMRequirement.getHeadline()).isEqualTo(DEFAULT_HEADLINE);
        assertThat(testRMRequirement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRMRequirement.getGeneralNote()).isEqualTo(DEFAULT_GENERAL_NOTE);
        assertThat(testRMRequirement.getTechnicalNotes()).isEqualTo(DEFAULT_TECHNICAL_NOTES);
        assertThat(testRMRequirement.getScope()).isEqualTo(DEFAULT_SCOPE);
        assertThat(testRMRequirement.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the RMRequirement in Elasticsearch
        verify(mockRMRequirementSearchRepository, times(1)).save(testRMRequirement);
    }

    @Test
    @Transactional
    public void createRMRequirementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMRequirementRepository.findAll().size();

        // Create the RMRequirement with an existing ID
        rMRequirement.setId(1L);
        RMRequirementDTO rMRequirementDTO = rMRequirementMapper.toDto(rMRequirement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMRequirementMockMvc.perform(post("/api/rm-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMRequirementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMRequirement in the database
        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMRequirement in Elasticsearch
        verify(mockRMRequirementSearchRepository, times(0)).save(rMRequirement);
    }

    @Test
    @Transactional
    public void checkFunctionalIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = rMRequirementRepository.findAll().size();
        // set the field null
        rMRequirement.setFunctionalID(null);

        // Create the RMRequirement, which fails.
        RMRequirementDTO rMRequirementDTO = rMRequirementMapper.toDto(rMRequirement);

        restRMRequirementMockMvc.perform(post("/api/rm-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMRequirementDTO)))
            .andExpect(status().isBadRequest());

        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRMRequirements() throws Exception {
        // Initialize the database
        rMRequirementRepository.saveAndFlush(rMRequirement);

        // Get all the rMRequirementList
        restRMRequirementMockMvc.perform(get("/api/rm-requirements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].functionalID").value(hasItem(DEFAULT_FUNCTIONAL_ID.toString())))
            .andExpect(jsonPath("$.[*].headline").value(hasItem(DEFAULT_HEADLINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].generalNote").value(hasItem(DEFAULT_GENERAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].technicalNotes").value(hasItem(DEFAULT_TECHNICAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getRMRequirement() throws Exception {
        // Initialize the database
        rMRequirementRepository.saveAndFlush(rMRequirement);

        // Get the rMRequirement
        restRMRequirementMockMvc.perform(get("/api/rm-requirements/{id}", rMRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMRequirement.getId().intValue()))
            .andExpect(jsonPath("$.functionalID").value(DEFAULT_FUNCTIONAL_ID.toString()))
            .andExpect(jsonPath("$.headline").value(DEFAULT_HEADLINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.generalNote").value(DEFAULT_GENERAL_NOTE.toString()))
            .andExpect(jsonPath("$.technicalNotes").value(DEFAULT_TECHNICAL_NOTES.toString()))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRMRequirement() throws Exception {
        // Get the rMRequirement
        restRMRequirementMockMvc.perform(get("/api/rm-requirements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMRequirement() throws Exception {
        // Initialize the database
        rMRequirementRepository.saveAndFlush(rMRequirement);

        int databaseSizeBeforeUpdate = rMRequirementRepository.findAll().size();

        // Update the rMRequirement
        RMRequirement updatedRMRequirement = rMRequirementRepository.findById(rMRequirement.getId()).get();
        // Disconnect from session so that the updates on updatedRMRequirement are not directly saved in db
        em.detach(updatedRMRequirement);
        updatedRMRequirement
            .functionalID(UPDATED_FUNCTIONAL_ID)
            .headline(UPDATED_HEADLINE)
            .description(UPDATED_DESCRIPTION)
            .generalNote(UPDATED_GENERAL_NOTE)
            .technicalNotes(UPDATED_TECHNICAL_NOTES)
            .scope(UPDATED_SCOPE)
            .type(UPDATED_TYPE);
        RMRequirementDTO rMRequirementDTO = rMRequirementMapper.toDto(updatedRMRequirement);

        restRMRequirementMockMvc.perform(put("/api/rm-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMRequirementDTO)))
            .andExpect(status().isOk());

        // Validate the RMRequirement in the database
        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeUpdate);
        RMRequirement testRMRequirement = rMRequirementList.get(rMRequirementList.size() - 1);
        assertThat(testRMRequirement.getFunctionalID()).isEqualTo(UPDATED_FUNCTIONAL_ID);
        assertThat(testRMRequirement.getHeadline()).isEqualTo(UPDATED_HEADLINE);
        assertThat(testRMRequirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRMRequirement.getGeneralNote()).isEqualTo(UPDATED_GENERAL_NOTE);
        assertThat(testRMRequirement.getTechnicalNotes()).isEqualTo(UPDATED_TECHNICAL_NOTES);
        assertThat(testRMRequirement.getScope()).isEqualTo(UPDATED_SCOPE);
        assertThat(testRMRequirement.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the RMRequirement in Elasticsearch
        verify(mockRMRequirementSearchRepository, times(1)).save(testRMRequirement);
    }

    @Test
    @Transactional
    public void updateNonExistingRMRequirement() throws Exception {
        int databaseSizeBeforeUpdate = rMRequirementRepository.findAll().size();

        // Create the RMRequirement
        RMRequirementDTO rMRequirementDTO = rMRequirementMapper.toDto(rMRequirement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMRequirementMockMvc.perform(put("/api/rm-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMRequirementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMRequirement in the database
        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMRequirement in Elasticsearch
        verify(mockRMRequirementSearchRepository, times(0)).save(rMRequirement);
    }

    @Test
    @Transactional
    public void deleteRMRequirement() throws Exception {
        // Initialize the database
        rMRequirementRepository.saveAndFlush(rMRequirement);

        int databaseSizeBeforeDelete = rMRequirementRepository.findAll().size();

        // Get the rMRequirement
        restRMRequirementMockMvc.perform(delete("/api/rm-requirements/{id}", rMRequirement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMRequirement> rMRequirementList = rMRequirementRepository.findAll();
        assertThat(rMRequirementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMRequirement in Elasticsearch
        verify(mockRMRequirementSearchRepository, times(1)).deleteById(rMRequirement.getId());
    }

    @Test
    @Transactional
    public void searchRMRequirement() throws Exception {
        // Initialize the database
        rMRequirementRepository.saveAndFlush(rMRequirement);
        when(mockRMRequirementSearchRepository.search(queryStringQuery("id:" + rMRequirement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rMRequirement), PageRequest.of(0, 1), 1));
        // Search the rMRequirement
        restRMRequirementMockMvc.perform(get("/api/_search/rm-requirements?query=id:" + rMRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].functionalID").value(hasItem(DEFAULT_FUNCTIONAL_ID.toString())))
            .andExpect(jsonPath("$.[*].headline").value(hasItem(DEFAULT_HEADLINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].generalNote").value(hasItem(DEFAULT_GENERAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].technicalNotes").value(hasItem(DEFAULT_TECHNICAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMRequirement.class);
        RMRequirement rMRequirement1 = new RMRequirement();
        rMRequirement1.setId(1L);
        RMRequirement rMRequirement2 = new RMRequirement();
        rMRequirement2.setId(rMRequirement1.getId());
        assertThat(rMRequirement1).isEqualTo(rMRequirement2);
        rMRequirement2.setId(2L);
        assertThat(rMRequirement1).isNotEqualTo(rMRequirement2);
        rMRequirement1.setId(null);
        assertThat(rMRequirement1).isNotEqualTo(rMRequirement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMRequirementDTO.class);
        RMRequirementDTO rMRequirementDTO1 = new RMRequirementDTO();
        rMRequirementDTO1.setId(1L);
        RMRequirementDTO rMRequirementDTO2 = new RMRequirementDTO();
        assertThat(rMRequirementDTO1).isNotEqualTo(rMRequirementDTO2);
        rMRequirementDTO2.setId(rMRequirementDTO1.getId());
        assertThat(rMRequirementDTO1).isEqualTo(rMRequirementDTO2);
        rMRequirementDTO2.setId(2L);
        assertThat(rMRequirementDTO1).isNotEqualTo(rMRequirementDTO2);
        rMRequirementDTO1.setId(null);
        assertThat(rMRequirementDTO1).isNotEqualTo(rMRequirementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMRequirementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMRequirementMapper.fromId(null)).isNull();
    }
}
