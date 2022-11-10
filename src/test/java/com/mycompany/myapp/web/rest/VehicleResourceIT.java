package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.criteria.VehicleCriteria;
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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENCE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENCE_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .relatedUserLogin(DEFAULT_RELATED_USER_LOGIN)
            .expenceCode(DEFAULT_EXPENCE_CODE)
            .isActive(DEFAULT_IS_ACTIVE);
        return vehicle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .relatedUserLogin(UPDATED_RELATED_USER_LOGIN)
            .expenceCode(UPDATED_EXPENCE_CODE)
            .isActive(UPDATED_IS_ACTIVE);
        return vehicle;
    }

    @BeforeEach
    public void initTest() {
        vehicle = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();
        // Create the Vehicle
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isCreated());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVehicle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicle.getRelatedUserLogin()).isEqualTo(DEFAULT_RELATED_USER_LOGIN);
        assertThat(testVehicle.getExpenceCode()).isEqualTo(DEFAULT_EXPENCE_CODE);
        assertThat(testVehicle.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);

        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRepository.findAll().size();
        // set the field null
        vehicle.setCode(null);

        // Create the Vehicle, which fails.

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isBadRequest());

        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleRepository.findAll().size();
        // set the field null
        vehicle.setName(null);

        // Create the Vehicle, which fails.

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isBadRequest());

        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].relatedUserLogin").value(hasItem(DEFAULT_RELATED_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].expenceCode").value(hasItem(DEFAULT_EXPENCE_CODE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.relatedUserLogin").value(DEFAULT_RELATED_USER_LOGIN))
            .andExpect(jsonPath("$.expenceCode").value(DEFAULT_EXPENCE_CODE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        Long id = vehicle.getId();

        defaultVehicleShouldBeFound("id.equals=" + id);
        defaultVehicleShouldNotBeFound("id.notEquals=" + id);

        defaultVehicleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehicleShouldNotBeFound("id.greaterThan=" + id);

        defaultVehicleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehicleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehiclesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where code equals to DEFAULT_CODE
        defaultVehicleShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the vehicleList where code equals to UPDATED_CODE
        defaultVehicleShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where code in DEFAULT_CODE or UPDATED_CODE
        defaultVehicleShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the vehicleList where code equals to UPDATED_CODE
        defaultVehicleShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where code is not null
        defaultVehicleShouldBeFound("code.specified=true");

        // Get all the vehicleList where code is null
        defaultVehicleShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByCodeContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where code contains DEFAULT_CODE
        defaultVehicleShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the vehicleList where code contains UPDATED_CODE
        defaultVehicleShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where code does not contain DEFAULT_CODE
        defaultVehicleShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the vehicleList where code does not contain UPDATED_CODE
        defaultVehicleShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name equals to DEFAULT_NAME
        defaultVehicleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vehicleList where name equals to UPDATED_NAME
        defaultVehicleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVehicleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vehicleList where name equals to UPDATED_NAME
        defaultVehicleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name is not null
        defaultVehicleShouldBeFound("name.specified=true");

        // Get all the vehicleList where name is null
        defaultVehicleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNameContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name contains DEFAULT_NAME
        defaultVehicleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vehicleList where name contains UPDATED_NAME
        defaultVehicleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name does not contain DEFAULT_NAME
        defaultVehicleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vehicleList where name does not contain UPDATED_NAME
        defaultVehicleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByRelatedUserLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where relatedUserLogin equals to DEFAULT_RELATED_USER_LOGIN
        defaultVehicleShouldBeFound("relatedUserLogin.equals=" + DEFAULT_RELATED_USER_LOGIN);

        // Get all the vehicleList where relatedUserLogin equals to UPDATED_RELATED_USER_LOGIN
        defaultVehicleShouldNotBeFound("relatedUserLogin.equals=" + UPDATED_RELATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void getAllVehiclesByRelatedUserLoginIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where relatedUserLogin in DEFAULT_RELATED_USER_LOGIN or UPDATED_RELATED_USER_LOGIN
        defaultVehicleShouldBeFound("relatedUserLogin.in=" + DEFAULT_RELATED_USER_LOGIN + "," + UPDATED_RELATED_USER_LOGIN);

        // Get all the vehicleList where relatedUserLogin equals to UPDATED_RELATED_USER_LOGIN
        defaultVehicleShouldNotBeFound("relatedUserLogin.in=" + UPDATED_RELATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void getAllVehiclesByRelatedUserLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where relatedUserLogin is not null
        defaultVehicleShouldBeFound("relatedUserLogin.specified=true");

        // Get all the vehicleList where relatedUserLogin is null
        defaultVehicleShouldNotBeFound("relatedUserLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByRelatedUserLoginContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where relatedUserLogin contains DEFAULT_RELATED_USER_LOGIN
        defaultVehicleShouldBeFound("relatedUserLogin.contains=" + DEFAULT_RELATED_USER_LOGIN);

        // Get all the vehicleList where relatedUserLogin contains UPDATED_RELATED_USER_LOGIN
        defaultVehicleShouldNotBeFound("relatedUserLogin.contains=" + UPDATED_RELATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void getAllVehiclesByRelatedUserLoginNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where relatedUserLogin does not contain DEFAULT_RELATED_USER_LOGIN
        defaultVehicleShouldNotBeFound("relatedUserLogin.doesNotContain=" + DEFAULT_RELATED_USER_LOGIN);

        // Get all the vehicleList where relatedUserLogin does not contain UPDATED_RELATED_USER_LOGIN
        defaultVehicleShouldBeFound("relatedUserLogin.doesNotContain=" + UPDATED_RELATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpenceCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expenceCode equals to DEFAULT_EXPENCE_CODE
        defaultVehicleShouldBeFound("expenceCode.equals=" + DEFAULT_EXPENCE_CODE);

        // Get all the vehicleList where expenceCode equals to UPDATED_EXPENCE_CODE
        defaultVehicleShouldNotBeFound("expenceCode.equals=" + UPDATED_EXPENCE_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpenceCodeIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expenceCode in DEFAULT_EXPENCE_CODE or UPDATED_EXPENCE_CODE
        defaultVehicleShouldBeFound("expenceCode.in=" + DEFAULT_EXPENCE_CODE + "," + UPDATED_EXPENCE_CODE);

        // Get all the vehicleList where expenceCode equals to UPDATED_EXPENCE_CODE
        defaultVehicleShouldNotBeFound("expenceCode.in=" + UPDATED_EXPENCE_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpenceCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expenceCode is not null
        defaultVehicleShouldBeFound("expenceCode.specified=true");

        // Get all the vehicleList where expenceCode is null
        defaultVehicleShouldNotBeFound("expenceCode.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByExpenceCodeContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expenceCode contains DEFAULT_EXPENCE_CODE
        defaultVehicleShouldBeFound("expenceCode.contains=" + DEFAULT_EXPENCE_CODE);

        // Get all the vehicleList where expenceCode contains UPDATED_EXPENCE_CODE
        defaultVehicleShouldNotBeFound("expenceCode.contains=" + UPDATED_EXPENCE_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpenceCodeNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expenceCode does not contain DEFAULT_EXPENCE_CODE
        defaultVehicleShouldNotBeFound("expenceCode.doesNotContain=" + DEFAULT_EXPENCE_CODE);

        // Get all the vehicleList where expenceCode does not contain UPDATED_EXPENCE_CODE
        defaultVehicleShouldBeFound("expenceCode.doesNotContain=" + UPDATED_EXPENCE_CODE);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isActive equals to DEFAULT_IS_ACTIVE
        defaultVehicleShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the vehicleList where isActive equals to UPDATED_IS_ACTIVE
        defaultVehicleShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultVehicleShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the vehicleList where isActive equals to UPDATED_IS_ACTIVE
        defaultVehicleShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isActive is not null
        defaultVehicleShouldBeFound("isActive.specified=true");

        // Get all the vehicleList where isActive is null
        defaultVehicleShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            vehicleRepository.saveAndFlush(vehicle);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        vehicle.setMerchant(merchant);
        vehicleRepository.saveAndFlush(vehicle);
        Long merchantId = merchant.getId();

        // Get all the vehicleList where merchant equals to merchantId
        defaultVehicleShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the vehicleList where merchant equals to (merchantId + 1)
        defaultVehicleShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].relatedUserLogin").value(hasItem(DEFAULT_RELATED_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].expenceCode").value(hasItem(DEFAULT_EXPENCE_CODE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).get();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .relatedUserLogin(UPDATED_RELATED_USER_LOGIN)
            .expenceCode(UPDATED_EXPENCE_CODE)
            .isActive(UPDATED_IS_ACTIVE);

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVehicle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicle.getRelatedUserLogin()).isEqualTo(UPDATED_RELATED_USER_LOGIN);
        assertThat(testVehicle.getExpenceCode()).isEqualTo(UPDATED_EXPENCE_CODE);
        assertThat(testVehicle.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle.expenceCode(UPDATED_EXPENCE_CODE).isActive(UPDATED_IS_ACTIVE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVehicle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicle.getRelatedUserLogin()).isEqualTo(DEFAULT_RELATED_USER_LOGIN);
        assertThat(testVehicle.getExpenceCode()).isEqualTo(UPDATED_EXPENCE_CODE);
        assertThat(testVehicle.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .relatedUserLogin(UPDATED_RELATED_USER_LOGIN)
            .expenceCode(UPDATED_EXPENCE_CODE)
            .isActive(UPDATED_IS_ACTIVE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVehicle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicle.getRelatedUserLogin()).isEqualTo(UPDATED_RELATED_USER_LOGIN);
        assertThat(testVehicle.getExpenceCode()).isEqualTo(UPDATED_EXPENCE_CODE);
        assertThat(testVehicle.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeDelete = vehicleRepository.findAll().size();

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
