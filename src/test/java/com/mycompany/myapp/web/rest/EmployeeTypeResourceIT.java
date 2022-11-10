package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.EmployeeType;
import com.mycompany.myapp.repository.EmployeeTypeRepository;
import com.mycompany.myapp.service.criteria.EmployeeTypeCriteria;
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
 * Integration tests for the {@link EmployeeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/employee-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeTypeMockMvc;

    private EmployeeType employeeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeType createEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType().code(DEFAULT_CODE).name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return employeeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeType createUpdatedEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType().code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return employeeType;
    }

    @BeforeEach
    public void initTest() {
        employeeType = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeType() throws Exception {
        int databaseSizeBeforeCreate = employeeTypeRepository.findAll().size();
        // Create the EmployeeType
        restEmployeeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isCreated());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmployeeType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployeeType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEmployeeTypeWithExistingId() throws Exception {
        // Create the EmployeeType with an existing ID
        employeeType.setId(1L);

        int databaseSizeBeforeCreate = employeeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeTypeRepository.findAll().size();
        // set the field null
        employeeType.setCode(null);

        // Create the EmployeeType, which fails.

        restEmployeeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeTypeRepository.findAll().size();
        // set the field null
        employeeType.setName(null);

        // Create the EmployeeType, which fails.

        restEmployeeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeTypes() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get the employeeType
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getEmployeeTypesByIdFiltering() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        Long id = employeeType.getId();

        defaultEmployeeTypeShouldBeFound("id.equals=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where code equals to DEFAULT_CODE
        defaultEmployeeTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the employeeTypeList where code equals to UPDATED_CODE
        defaultEmployeeTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEmployeeTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the employeeTypeList where code equals to UPDATED_CODE
        defaultEmployeeTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where code is not null
        defaultEmployeeTypeShouldBeFound("code.specified=true");

        // Get all the employeeTypeList where code is null
        defaultEmployeeTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where code contains DEFAULT_CODE
        defaultEmployeeTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the employeeTypeList where code contains UPDATED_CODE
        defaultEmployeeTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where code does not contain DEFAULT_CODE
        defaultEmployeeTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the employeeTypeList where code does not contain UPDATED_CODE
        defaultEmployeeTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where name equals to DEFAULT_NAME
        defaultEmployeeTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the employeeTypeList where name equals to UPDATED_NAME
        defaultEmployeeTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEmployeeTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the employeeTypeList where name equals to UPDATED_NAME
        defaultEmployeeTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where name is not null
        defaultEmployeeTypeShouldBeFound("name.specified=true");

        // Get all the employeeTypeList where name is null
        defaultEmployeeTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where name contains DEFAULT_NAME
        defaultEmployeeTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the employeeTypeList where name contains UPDATED_NAME
        defaultEmployeeTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where name does not contain DEFAULT_NAME
        defaultEmployeeTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the employeeTypeList where name does not contain UPDATED_NAME
        defaultEmployeeTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEmployeeTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the employeeTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the employeeTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive is not null
        defaultEmployeeTypeShouldBeFound("isActive.specified=true");

        // Get all the employeeTypeList where isActive is null
        defaultEmployeeTypeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeTypesByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employeeTypeRepository.saveAndFlush(employeeType);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        employeeType.addEmployee(employee);
        employeeTypeRepository.saveAndFlush(employeeType);
        Long employeeId = employee.getId();

        // Get all the employeeTypeList where employee equals to employeeId
        defaultEmployeeTypeShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employeeTypeList where employee equals to (employeeId + 1)
        defaultEmployeeTypeShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeTypeShouldBeFound(String filter) throws Exception {
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeTypeShouldNotBeFound(String filter) throws Exception {
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeType() throws Exception {
        // Get the employeeType
        restEmployeeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();

        // Update the employeeType
        EmployeeType updatedEmployeeType = employeeTypeRepository.findById(employeeType.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeType are not directly saved in db
        em.detach(updatedEmployeeType);
        updatedEmployeeType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmployeeType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeeType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeTypeWithPatch() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();

        // Update the employeeType using partial update
        EmployeeType partialUpdatedEmployeeType = new EmployeeType();
        partialUpdatedEmployeeType.setId(employeeType.getId());

        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmployeeType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployeeType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeTypeWithPatch() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();

        // Update the employeeType using partial update
        EmployeeType partialUpdatedEmployeeType = new EmployeeType();
        partialUpdatedEmployeeType.setId(employeeType.getId());

        partialUpdatedEmployeeType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmployeeType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeeType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();
        employeeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        int databaseSizeBeforeDelete = employeeTypeRepository.findAll().size();

        // Delete the employeeType
        restEmployeeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
