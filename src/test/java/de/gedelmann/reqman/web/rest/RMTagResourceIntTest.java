package de.gedelmann.reqman.web.rest;

import de.gedelmann.reqman.ReqmanApp;

import de.gedelmann.reqman.domain.RMTag;
import de.gedelmann.reqman.repository.RMTagRepository;
import de.gedelmann.reqman.repository.search.RMTagSearchRepository;
import de.gedelmann.reqman.service.RMTagService;
import de.gedelmann.reqman.service.dto.RMTagDTO;
import de.gedelmann.reqman.service.mapper.RMTagMapper;
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

import de.gedelmann.reqman.domain.enumeration.RMTagType;
/**
 * Test class for the RMTagResource REST controller.
 *
 * @see RMTagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReqmanApp.class)
public class RMTagResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final RMTagType DEFAULT_TYPE = RMTagType.GENERATED;
    private static final RMTagType UPDATED_TYPE = RMTagType.MANUAL;

    @Autowired
    private RMTagRepository rMTagRepository;
    @Mock
    private RMTagRepository rMTagRepositoryMock;

    @Autowired
    private RMTagMapper rMTagMapper;
    
    @Mock
    private RMTagService rMTagServiceMock;

    @Autowired
    private RMTagService rMTagService;

    /**
     * This repository is mocked in the de.gedelmann.reqman.repository.search test package.
     *
     * @see de.gedelmann.reqman.repository.search.RMTagSearchRepositoryMockConfiguration
     */
    @Autowired
    private RMTagSearchRepository mockRMTagSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRMTagMockMvc;

    private RMTag rMTag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RMTagResource rMTagResource = new RMTagResource(rMTagService);
        this.restRMTagMockMvc = MockMvcBuilders.standaloneSetup(rMTagResource)
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
    public static RMTag createEntity(EntityManager em) {
        RMTag rMTag = new RMTag()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return rMTag;
    }

    @Before
    public void initTest() {
        rMTag = createEntity(em);
    }

    @Test
    @Transactional
    public void createRMTag() throws Exception {
        int databaseSizeBeforeCreate = rMTagRepository.findAll().size();

        // Create the RMTag
        RMTagDTO rMTagDTO = rMTagMapper.toDto(rMTag);
        restRMTagMockMvc.perform(post("/api/rm-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMTagDTO)))
            .andExpect(status().isCreated());

        // Validate the RMTag in the database
        List<RMTag> rMTagList = rMTagRepository.findAll();
        assertThat(rMTagList).hasSize(databaseSizeBeforeCreate + 1);
        RMTag testRMTag = rMTagList.get(rMTagList.size() - 1);
        assertThat(testRMTag.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRMTag.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the RMTag in Elasticsearch
        verify(mockRMTagSearchRepository, times(1)).save(testRMTag);
    }

    @Test
    @Transactional
    public void createRMTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rMTagRepository.findAll().size();

        // Create the RMTag with an existing ID
        rMTag.setId(1L);
        RMTagDTO rMTagDTO = rMTagMapper.toDto(rMTag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMTagMockMvc.perform(post("/api/rm-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMTag in the database
        List<RMTag> rMTagList = rMTagRepository.findAll();
        assertThat(rMTagList).hasSize(databaseSizeBeforeCreate);

        // Validate the RMTag in Elasticsearch
        verify(mockRMTagSearchRepository, times(0)).save(rMTag);
    }

    @Test
    @Transactional
    public void getAllRMTags() throws Exception {
        // Initialize the database
        rMTagRepository.saveAndFlush(rMTag);

        // Get all the rMTagList
        restRMTagMockMvc.perform(get("/api/rm-tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    public void getAllRMTagsWithEagerRelationshipsIsEnabled() throws Exception {
        RMTagResource rMTagResource = new RMTagResource(rMTagServiceMock);
        when(rMTagServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRMTagMockMvc = MockMvcBuilders.standaloneSetup(rMTagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRMTagMockMvc.perform(get("/api/rm-tags?eagerload=true"))
        .andExpect(status().isOk());

        verify(rMTagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllRMTagsWithEagerRelationshipsIsNotEnabled() throws Exception {
        RMTagResource rMTagResource = new RMTagResource(rMTagServiceMock);
            when(rMTagServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRMTagMockMvc = MockMvcBuilders.standaloneSetup(rMTagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRMTagMockMvc.perform(get("/api/rm-tags?eagerload=true"))
        .andExpect(status().isOk());

            verify(rMTagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRMTag() throws Exception {
        // Initialize the database
        rMTagRepository.saveAndFlush(rMTag);

        // Get the rMTag
        restRMTagMockMvc.perform(get("/api/rm-tags/{id}", rMTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rMTag.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRMTag() throws Exception {
        // Get the rMTag
        restRMTagMockMvc.perform(get("/api/rm-tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRMTag() throws Exception {
        // Initialize the database
        rMTagRepository.saveAndFlush(rMTag);

        int databaseSizeBeforeUpdate = rMTagRepository.findAll().size();

        // Update the rMTag
        RMTag updatedRMTag = rMTagRepository.findById(rMTag.getId()).get();
        // Disconnect from session so that the updates on updatedRMTag are not directly saved in db
        em.detach(updatedRMTag);
        updatedRMTag
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        RMTagDTO rMTagDTO = rMTagMapper.toDto(updatedRMTag);

        restRMTagMockMvc.perform(put("/api/rm-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMTagDTO)))
            .andExpect(status().isOk());

        // Validate the RMTag in the database
        List<RMTag> rMTagList = rMTagRepository.findAll();
        assertThat(rMTagList).hasSize(databaseSizeBeforeUpdate);
        RMTag testRMTag = rMTagList.get(rMTagList.size() - 1);
        assertThat(testRMTag.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRMTag.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the RMTag in Elasticsearch
        verify(mockRMTagSearchRepository, times(1)).save(testRMTag);
    }

    @Test
    @Transactional
    public void updateNonExistingRMTag() throws Exception {
        int databaseSizeBeforeUpdate = rMTagRepository.findAll().size();

        // Create the RMTag
        RMTagDTO rMTagDTO = rMTagMapper.toDto(rMTag);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRMTagMockMvc.perform(put("/api/rm-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rMTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RMTag in the database
        List<RMTag> rMTagList = rMTagRepository.findAll();
        assertThat(rMTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RMTag in Elasticsearch
        verify(mockRMTagSearchRepository, times(0)).save(rMTag);
    }

    @Test
    @Transactional
    public void deleteRMTag() throws Exception {
        // Initialize the database
        rMTagRepository.saveAndFlush(rMTag);

        int databaseSizeBeforeDelete = rMTagRepository.findAll().size();

        // Get the rMTag
        restRMTagMockMvc.perform(delete("/api/rm-tags/{id}", rMTag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RMTag> rMTagList = rMTagRepository.findAll();
        assertThat(rMTagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RMTag in Elasticsearch
        verify(mockRMTagSearchRepository, times(1)).deleteById(rMTag.getId());
    }

    @Test
    @Transactional
    public void searchRMTag() throws Exception {
        // Initialize the database
        rMTagRepository.saveAndFlush(rMTag);
        when(mockRMTagSearchRepository.search(queryStringQuery("id:" + rMTag.getId())))
            .thenReturn(Collections.singletonList(rMTag));
        // Search the rMTag
        restRMTagMockMvc.perform(get("/api/_search/rm-tags?query=id:" + rMTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMTag.class);
        RMTag rMTag1 = new RMTag();
        rMTag1.setId(1L);
        RMTag rMTag2 = new RMTag();
        rMTag2.setId(rMTag1.getId());
        assertThat(rMTag1).isEqualTo(rMTag2);
        rMTag2.setId(2L);
        assertThat(rMTag1).isNotEqualTo(rMTag2);
        rMTag1.setId(null);
        assertThat(rMTag1).isNotEqualTo(rMTag2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMTagDTO.class);
        RMTagDTO rMTagDTO1 = new RMTagDTO();
        rMTagDTO1.setId(1L);
        RMTagDTO rMTagDTO2 = new RMTagDTO();
        assertThat(rMTagDTO1).isNotEqualTo(rMTagDTO2);
        rMTagDTO2.setId(rMTagDTO1.getId());
        assertThat(rMTagDTO1).isEqualTo(rMTagDTO2);
        rMTagDTO2.setId(2L);
        assertThat(rMTagDTO1).isNotEqualTo(rMTagDTO2);
        rMTagDTO1.setId(null);
        assertThat(rMTagDTO1).isNotEqualTo(rMTagDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rMTagMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rMTagMapper.fromId(null)).isNull();
    }
}
