package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMProject;
import de.gedelmann.reqman.repository.RMProjectRepository;
import de.gedelmann.reqman.repository.search.RMProjectSearchRepository;
import de.gedelmann.reqman.service.RMProjectService;
import de.gedelmann.reqman.service.dto.RMProjectDTO;
import de.gedelmann.reqman.service.mapper.RMProjectMapper;
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

/**
 * Test class for the RMProjectResource REST controller.
 *
 * @see RMProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RMProjectRepository rMProjectRepository;


    @Autowired
    private RMProjectMapper rMProjectMapper;
    

    @Autowired
    private RMProjectService rMProjectService;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMProjectSearchRepositoryMockConfiguration
     */
    @Autowired
    private RMProjectSearchRepository mockRMProjectSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMProjectMockMvc;

    private RMProject rMProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMProjectResource rMProjectResource = new RMProjectResource(rMProjectService);
        this.restRMProjectMockMvc = MockMvcBuilders.standaloneSetup(rMProjectResource)
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
    public static RMProject createEntity(EntityManager em) {
        RMProject rMProject = new RMProject()
            .name(DEFAULT_NAME);
        return rMProject;
    }

    @Before
    public void initTest() {
        rMProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMProject() throws Exception {
        int databaseSizeBeforeCreate = rMProjectRepository.findAll().size();

        // Create the RMProject
        RMProjectDTO rMProjectDTO = rMProjectMapper.toDto(rMProject);
        restRMProjectMockMvc.perform(post("/api/rm-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the RMProject in the database
        List<RMProject> rMProjectList = rMProjectRepository.findAll();
        assertThat(rMProjectList).hasSize(databaseSizeBeforeCreate + 1);
        RMProject testRMProject = rMProjectList.get(rMProjectList.size() - 1);
        assertThat(testRMProject.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the RMProject in Elasticsearch
        verify(mockRMProjectSearchRepository, times(1)).save(testRMProject);
    }

    @Test
    @Transactional
    public void createRMProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMProjectRepository.findAll().size();

        // Create the RMProject with an existing ID
        rMProject.setId(1L);
        RMProjectDTO rMProjectDTO = rMProjectMapper.toDto(rMProject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMProjectMockMvc.perform(post("/api/rm-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMProject in the database
        List<RMProject> rMProjectList = rMProjectRepository.findAll();
        assertThat(rMProjectList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMProject in Elasticsearch
        verify(mockRMProjectSearchRepository, times(0)).save(rMProject);
    }

    @Test
    @Transactional
    public void getAllRMProjects() throws Exception {
        // Initialize the database
        rMProjectRepository.saveAndFlush(rMProject);

        // Get all the rMProjectList
        restRMProjectMockMvc.perform(get("/api/rm-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getRMProject() throws Exception {
        // Initialize the database
        rMProjectRepository.saveAndFlush(rMProject);

        // Get the rMProject
        restRMProjectMockMvc.perform(get("/api/rm-projects/{id}", rMProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRMProject() throws Exception {
        // Get the rMProject
        restRMProjectMockMvc.perform(get("/api/rm-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMProject() throws Exception {
        // Initialize the database
        rMProjectRepository.saveAndFlush(rMProject);

        int databaseSizeBeforeUpdate = rMProjectRepository.findAll().size();

        // Update the rMProject
        RMProject updatedRMProject = rMProjectRepository.findById(rMProject.getId()).get();
        // Disconnect from session so that the updates on updatedRMProject are not directly saved in db
        em.detach(updatedRMProject);
        updatedRMProject
            .name(UPDATED_NAME);
        RMProjectDTO rMProjectDTO = rMProjectMapper.toDto(updatedRMProject);

        restRMProjectMockMvc.perform(put("/api/rm-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMProjectDTO)))
            .andExpect(status().isOk());

        // Validate the RMProject in the database
        List<RMProject> rMProjectList = rMProjectRepository.findAll();
        assertThat(rMProjectList).hasSize(databaseSizeBeforeUpdate);
        RMProject testRMProject = rMProjectList.get(rMProjectList.size() - 1);
        assertThat(testRMProject.getName()).isEqualTo(UPDATED_NAME);

        // Validate the RMProject in Elasticsearch
        verify(mockRMProjectSearchRepository, times(1)).save(testRMProject);
    }

    @Test
    @Transactional
    public void updateNonExistingRMProject() throws Exception {
        int databaseSizeBeforeUpdate = rMProjectRepository.findAll().size();

        // Create the RMProject
        RMProjectDTO rMProjectDTO = rMProjectMapper.toDto(rMProject);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMProjectMockMvc.perform(put("/api/rm-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMProject in the database
        List<RMProject> rMProjectList = rMProjectRepository.findAll();
        assertThat(rMProjectList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMProject in Elasticsearch
        verify(mockRMProjectSearchRepository, times(0)).save(rMProject);
    }

    @Test
    @Transactional
    public void deleteRMProject() throws Exception {
        // Initialize the database
        rMProjectRepository.saveAndFlush(rMProject);

        int databaseSizeBeforeDelete = rMProjectRepository.findAll().size();

        // Get the rMProject
        restRMProjectMockMvc.perform(delete("/api/rm-projects/{id}", rMProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMProject> rMProjectList = rMProjectRepository.findAll();
        assertThat(rMProjectList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMProject in Elasticsearch
        verify(mockRMProjectSearchRepository, times(1)).deleteById(rMProject.getId());
    }

    @Test
    @Transactional
    public void searchRMProject() throws Exception {
        // Initialize the database
        rMProjectRepository.saveAndFlush(rMProject);
        when(mockRMProjectSearchRepository.search(queryStringQuery("id:" + rMProject.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rMProject), PageRequest.of(0, 1), 1));
        // Search the rMProject
        restRMProjectMockMvc.perform(get("/api/_search/rm-projects?query=id:" + rMProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMProject.class);
        RMProject rMProject1 = new RMProject();
        rMProject1.setId(1L);
        RMProject rMProject2 = new RMProject();
        rMProject2.setId(rMProject1.getId());
        assertThat(rMProject1).isEqualTo(rMProject2);
        rMProject2.setId(2L);
        assertThat(rMProject1).isNotEqualTo(rMProject2);
        rMProject1.setId(null);
        assertThat(rMProject1).isNotEqualTo(rMProject2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMProjectDTO.class);
        RMProjectDTO rMProjectDTO1 = new RMProjectDTO();
        rMProjectDTO1.setId(1L);
        RMProjectDTO rMProjectDTO2 = new RMProjectDTO();
        assertThat(rMProjectDTO1).isNotEqualTo(rMProjectDTO2);
        rMProjectDTO2.setId(rMProjectDTO1.getId());
        assertThat(rMProjectDTO1).isEqualTo(rMProjectDTO2);
        rMProjectDTO2.setId(2L);
        assertThat(rMProjectDTO1).isNotEqualTo(rMProjectDTO2);
        rMProjectDTO1.setId(null);
        assertThat(rMProjectDTO1).isNotEqualTo(rMProjectDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMProjectMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMProjectMapper.fromId(null)).isNull();
    }
}
