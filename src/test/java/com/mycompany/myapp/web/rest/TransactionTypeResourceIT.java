package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.TransactionTypeRepository;
import com.mycompany.myapp.service.criteria.TransactionTypeCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return transactionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return transactionType;
    }

    @BeforeEach
    public void initTest() {
        transactionType = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();
        // Create the TransactionType
        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTransactionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);

        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionTypeRepository.findAll().size();
        // set the field null
        transactionType.setCode(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionTypeRepository.findAll().size();
        // set the field null
        transactionType.setDescription(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        Long id = transactionType.getId();

        defaultTransactionTypeShouldBeFound("id.equals=" + id);
        defaultTransactionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code equals to DEFAULT_CODE
        defaultTransactionTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the transactionTypeList where code equals to UPDATED_CODE
        defaultTransactionTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTransactionTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the transactionTypeList where code equals to UPDATED_CODE
        defaultTransactionTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code is not null
        defaultTransactionTypeShouldBeFound("code.specified=true");

        // Get all the transactionTypeList where code is null
        defaultTransactionTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code contains DEFAULT_CODE
        defaultTransactionTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the transactionTypeList where code contains UPDATED_CODE
        defaultTransactionTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code does not contain DEFAULT_CODE
        defaultTransactionTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the transactionTypeList where code does not contain UPDATED_CODE
        defaultTransactionTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description equals to DEFAULT_DESCRIPTION
        defaultTransactionTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionTypeList where description equals to UPDATED_DESCRIPTION
        defaultTransactionTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTransactionTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the transactionTypeList where description equals to UPDATED_DESCRIPTION
        defaultTransactionTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description is not null
        defaultTransactionTypeShouldBeFound("description.specified=true");

        // Get all the transactionTypeList where description is null
        defaultTransactionTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description contains DEFAULT_DESCRIPTION
        defaultTransactionTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the transactionTypeList where description contains UPDATED_DESCRIPTION
        defaultTransactionTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultTransactionTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the transactionTypeList where description does not contain UPDATED_DESCRIPTION
        defaultTransactionTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTransactionTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the transactionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the transactionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive is not null
        defaultTransactionTypeShouldBeFound("isActive.specified=true");

        // Get all the transactionTypeList where isActive is null
        defaultTransactionTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionTypeShouldBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTransactionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.isActive(UPDATED_IS_ACTIVE);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTransactionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTransactionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeDelete = transactionTypeRepository.findAll().size();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
