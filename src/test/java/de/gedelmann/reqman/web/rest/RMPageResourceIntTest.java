package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMPage;
import de.gedelmann.reqman.repository.RMPageRepository;
import de.gedelmann.reqman.repository.search.RMPageSearchRepository;
import de.gedelmann.reqman.service.RMPageService;
import de.gedelmann.reqman.service.dto.RMPageDTO;
import de.gedelmann.reqman.service.mapper.RMPageMapper;
import de.gedelmann.reqman.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

/**
 * Test class for the RMPageResource REST controller.
 *
 * @see RMPageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMPageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RMPageRepository rMPageRepository;


    @Autowired
    private RMPageMapper rMPageMapper;
    

    @Autowired
    private RMPageService rMPageService;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMPageSearchRepositoryMockConfiguration
     */
    @Autowired
    private RMPageSearchRepository mockRMPageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMPageMockMvc;

    private RMPage rMPage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMPageResource rMPageResource = new RMPageResource(rMPageService);
        this.restRMPageMockMvc = MockMvcBuilders.standaloneSetup(rMPageResource)
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
    public static RMPage createEntity(EntityManager em) {
        RMPage rMPage = new RMPage()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return rMPage;
    }

    @Before
    public void initTest() {
        rMPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMPage() throws Exception {
        int databaseSizeBeforeCreate = rMPageRepository.findAll().size();

        // Create the RMPage
        RMPageDTO rMPageDTO = rMPageMapper.toDto(rMPage);
        restRMPageMockMvc.perform(post("/api/rm-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMPageDTO)))
            .andExpect(status().isCreated());

        // Validate the RMPage in the database
        List<RMPage> rMPageList = rMPageRepository.findAll();
        assertThat(rMPageList).hasSize(databaseSizeBeforeCreate + 1);
        RMPage testRMPage = rMPageList.get(rMPageList.size() - 1);
        assertThat(testRMPage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRMPage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the RMPage in Elasticsearch
        verify(mockRMPageSearchRepository, times(1)).save(testRMPage);
    }

    @Test
    @Transactional
    public void createRMPageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMPageRepository.findAll().size();

        // Create the RMPage with an existing ID
        rMPage.setId(1L);
        RMPageDTO rMPageDTO = rMPageMapper.toDto(rMPage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMPageMockMvc.perform(post("/api/rm-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMPageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMPage in the database
        List<RMPage> rMPageList = rMPageRepository.findAll();
        assertThat(rMPageList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMPage in Elasticsearch
        verify(mockRMPageSearchRepository, times(0)).save(rMPage);
    }

    @Test
    @Transactional
    public void getAllRMPages() throws Exception {
        // Initialize the database
        rMPageRepository.saveAndFlush(rMPage);

        // Get all the rMPageList
        restRMPageMockMvc.perform(get("/api/rm-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getRMPage() throws Exception {
        // Initialize the database
        rMPageRepository.saveAndFlush(rMPage);

        // Get the rMPage
        restRMPageMockMvc.perform(get("/api/rm-pages/{id}", rMPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMPage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRMPage() throws Exception {
        // Get the rMPage
        restRMPageMockMvc.perform(get("/api/rm-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMPage() throws Exception {
        // Initialize the database
        rMPageRepository.saveAndFlush(rMPage);

        int databaseSizeBeforeUpdate = rMPageRepository.findAll().size();

        // Update the rMPage
        RMPage updatedRMPage = rMPageRepository.findById(rMPage.getId()).get();
        // Disconnect from session so that the updates on updatedRMPage are not directly saved in db
        em.detach(updatedRMPage);
        updatedRMPage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        RMPageDTO rMPageDTO = rMPageMapper.toDto(updatedRMPage);

        restRMPageMockMvc.perform(put("/api/rm-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMPageDTO)))
            .andExpect(status().isOk());

        // Validate the RMPage in the database
        List<RMPage> rMPageList = rMPageRepository.findAll();
        assertThat(rMPageList).hasSize(databaseSizeBeforeUpdate);
        RMPage testRMPage = rMPageList.get(rMPageList.size() - 1);
        assertThat(testRMPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRMPage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the RMPage in Elasticsearch
        verify(mockRMPageSearchRepository, times(1)).save(testRMPage);
    }

    @Test
    @Transactional
    public void updateNonExistingRMPage() throws Exception {
        int databaseSizeBeforeUpdate = rMPageRepository.findAll().size();

        // Create the RMPage
        RMPageDTO rMPageDTO = rMPageMapper.toDto(rMPage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMPageMockMvc.perform(put("/api/rm-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMPageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMPage in the database
        List<RMPage> rMPageList = rMPageRepository.findAll();
        assertThat(rMPageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMPage in Elasticsearch
        verify(mockRMPageSearchRepository, times(0)).save(rMPage);
    }

    @Test
    @Transactional
    public void deleteRMPage() throws Exception {
        // Initialize the database
        rMPageRepository.saveAndFlush(rMPage);

        int databaseSizeBeforeDelete = rMPageRepository.findAll().size();

        // Get the rMPage
        restRMPageMockMvc.perform(delete("/api/rm-pages/{id}", rMPage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMPage> rMPageList = rMPageRepository.findAll();
        assertThat(rMPageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMPage in Elasticsearch
        verify(mockRMPageSearchRepository, times(1)).deleteById(rMPage.getId());
    }

    @Test
    @Transactional
    public void searchRMPage() throws Exception {
        // Initialize the database
        rMPageRepository.saveAndFlush(rMPage);
        when(mockRMPageSearchRepository.search(queryStringQuery("id:" + rMPage.getId())))
            .thenReturn(Collections.singletonList(rMPage));
        // Search the rMPage
        restRMPageMockMvc.perform(get("/api/_search/rm-pages?query=id:" + rMPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMPage.class);
        RMPage rMPage1 = new RMPage();
        rMPage1.setId(1L);
        RMPage rMPage2 = new RMPage();
        rMPage2.setId(rMPage1.getId());
        assertThat(rMPage1).isEqualTo(rMPage2);
        rMPage2.setId(2L);
        assertThat(rMPage1).isNotEqualTo(rMPage2);
        rMPage1.setId(null);
        assertThat(rMPage1).isNotEqualTo(rMPage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMPageDTO.class);
        RMPageDTO rMPageDTO1 = new RMPageDTO();
        rMPageDTO1.setId(1L);
        RMPageDTO rMPageDTO2 = new RMPageDTO();
        assertThat(rMPageDTO1).isNotEqualTo(rMPageDTO2);
        rMPageDTO2.setId(rMPageDTO1.getId());
        assertThat(rMPageDTO1).isEqualTo(rMPageDTO2);
        rMPageDTO2.setId(2L);
        assertThat(rMPageDTO1).isNotEqualTo(rMPageDTO2);
        rMPageDTO1.setId(null);
        assertThat(rMPageDTO1).isNotEqualTo(rMPageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMPageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMPageMapper.fromId(null)).isNull();
    }
}
