package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMCategory;
import de.gedelmann.reqman.repository.RMCategoryRepository;
import de.gedelmann.reqman.repository.search.RMCategorySearchRepository;
import de.gedelmann.reqman.service.dto.RMCategoryDTO;
import de.gedelmann.reqman.service.mapper.RMCategoryMapper;
import de.gedelmann.reqman.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the RMCategoryResource REST controller.
 *
 * @see RMCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RMCategoryRepository rMCategoryRepository;
    @Mock
    private RMCategoryRepository rMCategoryRepositoryMock;

    @Autowired
    private RMCategoryMapper rMCategoryMapper;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private RMCategorySearchRepository mockRMCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMCategoryMockMvc;

    private RMCategory rMCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMCategoryResource rMCategoryResource = new RMCategoryResource(rMCategoryRepository, rMCategoryMapper, mockRMCategorySearchRepository);
        this.restRMCategoryMockMvc = MockMvcBuilders.standaloneSetup(rMCategoryResource)
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
    public static RMCategory createEntity(EntityManager em) {
        RMCategory rMCategory = new RMCategory()
            .name(DEFAULT_NAME);
        return rMCategory;
    }

    @Before
    public void initTest() {
        rMCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMCategory() throws Exception {
        int databaseSizeBeforeCreate = rMCategoryRepository.findAll().size();

        // Create the RMCategory
        RMCategoryDTO rMCategoryDTO = rMCategoryMapper.toDto(rMCategory);
        restRMCategoryMockMvc.perform(post("/api/rm-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RMCategory in the database
        List<RMCategory> rMCategoryList = rMCategoryRepository.findAll();
        assertThat(rMCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        RMCategory testRMCategory = rMCategoryList.get(rMCategoryList.size() - 1);
        assertThat(testRMCategory.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the RMCategory in Elasticsearch
        verify(mockRMCategorySearchRepository, times(1)).save(testRMCategory);
    }

    @Test
    @Transactional
    public void createRMCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMCategoryRepository.findAll().size();

        // Create the RMCategory with an existing ID
        rMCategory.setId(1L);
        RMCategoryDTO rMCategoryDTO = rMCategoryMapper.toDto(rMCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMCategoryMockMvc.perform(post("/api/rm-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMCategory in the database
        List<RMCategory> rMCategoryList = rMCategoryRepository.findAll();
        assertThat(rMCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMCategory in Elasticsearch
        verify(mockRMCategorySearchRepository, times(0)).save(rMCategory);
    }

    @Test
    @Transactional
    public void getAllRMCategories() throws Exception {
        // Initialize the database
        rMCategoryRepository.saveAndFlush(rMCategory);

        // Get all the rMCategoryList
        restRMCategoryMockMvc.perform(get("/api/rm-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    public void getAllRMCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        RMCategoryResource rMCategoryResource = new RMCategoryResource(rMCategoryRepositoryMock, rMCategoryMapper, mockRMCategorySearchRepository);
        when(rMCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRMCategoryMockMvc = MockMvcBuilders.standaloneSetup(rMCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRMCategoryMockMvc.perform(get("/api/rm-categories?eagerload=true"))
        .andExpect(status().isOk());

        verify(rMCategoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllRMCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        RMCategoryResource rMCategoryResource = new RMCategoryResource(rMCategoryRepositoryMock, rMCategoryMapper, mockRMCategorySearchRepository);
            when(rMCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRMCategoryMockMvc = MockMvcBuilders.standaloneSetup(rMCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRMCategoryMockMvc.perform(get("/api/rm-categories?eagerload=true"))
        .andExpect(status().isOk());

            verify(rMCategoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRMCategory() throws Exception {
        // Initialize the database
        rMCategoryRepository.saveAndFlush(rMCategory);

        // Get the rMCategory
        restRMCategoryMockMvc.perform(get("/api/rm-categories/{id}", rMCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRMCategory() throws Exception {
        // Get the rMCategory
        restRMCategoryMockMvc.perform(get("/api/rm-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMCategory() throws Exception {
        // Initialize the database
        rMCategoryRepository.saveAndFlush(rMCategory);

        int databaseSizeBeforeUpdate = rMCategoryRepository.findAll().size();

        // Update the rMCategory
        RMCategory updatedRMCategory = rMCategoryRepository.findById(rMCategory.getId()).get();
        // Disconnect from session so that the updates on updatedRMCategory are not directly saved in db
        em.detach(updatedRMCategory);
        updatedRMCategory
            .name(UPDATED_NAME);
        RMCategoryDTO rMCategoryDTO = rMCategoryMapper.toDto(updatedRMCategory);

        restRMCategoryMockMvc.perform(put("/api/rm-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the RMCategory in the database
        List<RMCategory> rMCategoryList = rMCategoryRepository.findAll();
        assertThat(rMCategoryList).hasSize(databaseSizeBeforeUpdate);
        RMCategory testRMCategory = rMCategoryList.get(rMCategoryList.size() - 1);
        assertThat(testRMCategory.getName()).isEqualTo(UPDATED_NAME);

        // Validate the RMCategory in Elasticsearch
        verify(mockRMCategorySearchRepository, times(1)).save(testRMCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingRMCategory() throws Exception {
        int databaseSizeBeforeUpdate = rMCategoryRepository.findAll().size();

        // Create the RMCategory
        RMCategoryDTO rMCategoryDTO = rMCategoryMapper.toDto(rMCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMCategoryMockMvc.perform(put("/api/rm-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMCategory in the database
        List<RMCategory> rMCategoryList = rMCategoryRepository.findAll();
        assertThat(rMCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMCategory in Elasticsearch
        verify(mockRMCategorySearchRepository, times(0)).save(rMCategory);
    }

    @Test
    @Transactional
    public void deleteRMCategory() throws Exception {
        // Initialize the database
        rMCategoryRepository.saveAndFlush(rMCategory);

        int databaseSizeBeforeDelete = rMCategoryRepository.findAll().size();

        // Get the rMCategory
        restRMCategoryMockMvc.perform(delete("/api/rm-categories/{id}", rMCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMCategory> rMCategoryList = rMCategoryRepository.findAll();
        assertThat(rMCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMCategory in Elasticsearch
        verify(mockRMCategorySearchRepository, times(1)).deleteById(rMCategory.getId());
    }

    @Test
    @Transactional
    public void searchRMCategory() throws Exception {
        // Initialize the database
        rMCategoryRepository.saveAndFlush(rMCategory);
        when(mockRMCategorySearchRepository.search(queryStringQuery("id:" + rMCategory.getId())))
            .thenReturn(Collections.singletonList(rMCategory));
        // Search the rMCategory
        restRMCategoryMockMvc.perform(get("/api/_search/rm-categories?query=id:" + rMCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMCategory.class);
        RMCategory rMCategory1 = new RMCategory();
        rMCategory1.setId(1L);
        RMCategory rMCategory2 = new RMCategory();
        rMCategory2.setId(rMCategory1.getId());
        assertThat(rMCategory1).isEqualTo(rMCategory2);
        rMCategory2.setId(2L);
        assertThat(rMCategory1).isNotEqualTo(rMCategory2);
        rMCategory1.setId(null);
        assertThat(rMCategory1).isNotEqualTo(rMCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMCategoryDTO.class);
        RMCategoryDTO rMCategoryDTO1 = new RMCategoryDTO();
        rMCategoryDTO1.setId(1L);
        RMCategoryDTO rMCategoryDTO2 = new RMCategoryDTO();
        assertThat(rMCategoryDTO1).isNotEqualTo(rMCategoryDTO2);
        rMCategoryDTO2.setId(rMCategoryDTO1.getId());
        assertThat(rMCategoryDTO1).isEqualTo(rMCategoryDTO2);
        rMCategoryDTO2.setId(2L);
        assertThat(rMCategoryDTO1).isNotEqualTo(rMCategoryDTO2);
        rMCategoryDTO1.setId(null);
        assertThat(rMCategoryDTO1).isNotEqualTo(rMCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMCategoryMapper.fromId(null)).isNull();
    }
}
