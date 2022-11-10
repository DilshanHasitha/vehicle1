package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExpenseType;
import com.mycompany.myapp.repository.ExpenseTypeRepository;
import com.mycompany.myapp.service.criteria.ExpenseTypeCriteria;
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
 * Integration tests for the {@link ExpenseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/expense-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseTypeMockMvc;

    private ExpenseType expenseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseType createEntity(EntityManager em) {
        ExpenseType expenseType = new ExpenseType().code(DEFAULT_CODE).name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return expenseType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseType createUpdatedEntity(EntityManager em) {
        ExpenseType expenseType = new ExpenseType().code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return expenseType;
    }

    @BeforeEach
    public void initTest() {
        expenseType = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseType() throws Exception {
        int databaseSizeBeforeCreate = expenseTypeRepository.findAll().size();
        // Create the ExpenseType
        restExpenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseType)))
            .andExpect(status().isCreated());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseType testExpenseType = expenseTypeList.get(expenseTypeList.size() - 1);
        assertThat(testExpenseType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExpenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExpenseType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createExpenseTypeWithExistingId() throws Exception {
        // Create the ExpenseType with an existing ID
        expenseType.setId(1L);

        int databaseSizeBeforeCreate = expenseTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseType)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseTypeRepository.findAll().size();
        // set the field null
        expenseType.setCode(null);

        // Create the ExpenseType, which fails.

        restExpenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseType)))
            .andExpect(status().isBadRequest());

        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseTypeRepository.findAll().size();
        // set the field null
        expenseType.setName(null);

        // Create the ExpenseType, which fails.

        restExpenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseType)))
            .andExpect(status().isBadRequest());

        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenseTypes() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get the expenseType
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getExpenseTypesByIdFiltering() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        Long id = expenseType.getId();

        defaultExpenseTypeShouldBeFound("id.equals=" + id);
        defaultExpenseTypeShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where code equals to DEFAULT_CODE
        defaultExpenseTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the expenseTypeList where code equals to UPDATED_CODE
        defaultExpenseTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultExpenseTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the expenseTypeList where code equals to UPDATED_CODE
        defaultExpenseTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where code is not null
        defaultExpenseTypeShouldBeFound("code.specified=true");

        // Get all the expenseTypeList where code is null
        defaultExpenseTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where code contains DEFAULT_CODE
        defaultExpenseTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the expenseTypeList where code contains UPDATED_CODE
        defaultExpenseTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where code does not contain DEFAULT_CODE
        defaultExpenseTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the expenseTypeList where code does not contain UPDATED_CODE
        defaultExpenseTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where name equals to DEFAULT_NAME
        defaultExpenseTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the expenseTypeList where name equals to UPDATED_NAME
        defaultExpenseTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExpenseTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the expenseTypeList where name equals to UPDATED_NAME
        defaultExpenseTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where name is not null
        defaultExpenseTypeShouldBeFound("name.specified=true");

        // Get all the expenseTypeList where name is null
        defaultExpenseTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where name contains DEFAULT_NAME
        defaultExpenseTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the expenseTypeList where name contains UPDATED_NAME
        defaultExpenseTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where name does not contain DEFAULT_NAME
        defaultExpenseTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the expenseTypeList where name does not contain UPDATED_NAME
        defaultExpenseTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExpenseTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the expenseTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExpenseTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the expenseTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExpenseTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList where isActive is not null
        defaultExpenseTypeShouldBeFound("isActive.specified=true");

        // Get all the expenseTypeList where isActive is null
        defaultExpenseTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseTypeShouldBeFound(String filter) throws Exception {
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseTypeShouldNotBeFound(String filter) throws Exception {
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseType() throws Exception {
        // Get the expenseType
        restExpenseTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();

        // Update the expenseType
        ExpenseType updatedExpenseType = expenseTypeRepository.findById(expenseType.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseType are not directly saved in db
        em.detach(updatedExpenseType);
        updatedExpenseType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
        ExpenseType testExpenseType = expenseTypeList.get(expenseTypeList.size() - 1);
        assertThat(testExpenseType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExpenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpenseType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseTypeWithPatch() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();

        // Update the expenseType using partial update
        ExpenseType partialUpdatedExpenseType = new ExpenseType();
        partialUpdatedExpenseType.setId(expenseType.getId());

        partialUpdatedExpenseType.code(UPDATED_CODE);

        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
        ExpenseType testExpenseType = expenseTypeList.get(expenseTypeList.size() - 1);
        assertThat(testExpenseType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExpenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExpenseType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateExpenseTypeWithPatch() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();

        // Update the expenseType using partial update
        ExpenseType partialUpdatedExpenseType = new ExpenseType();
        partialUpdatedExpenseType.setId(expenseType.getId());

        partialUpdatedExpenseType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
        ExpenseType testExpenseType = expenseTypeList.get(expenseTypeList.size() - 1);
        assertThat(testExpenseType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExpenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpenseType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseType() throws Exception {
        int databaseSizeBeforeUpdate = expenseTypeRepository.findAll().size();
        expenseType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expenseType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseType in the database
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        int databaseSizeBeforeDelete = expenseTypeRepository.findAll().size();

        // Delete the expenseType
        restExpenseTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseType> expenseTypeList = expenseTypeRepository.findAll();
        assertThat(expenseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
