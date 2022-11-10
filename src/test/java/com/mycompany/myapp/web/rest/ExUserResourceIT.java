package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ExUserRepository;
import com.mycompany.myapp.service.criteria.ExUserCriteria;
import java.math.BigDecimal;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExUserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "g@`b.K{/L<";
    private static final String UPDATED_EMAIL = "-_Q0a@gpUC.$";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_USER_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_USER_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_USER_LIMIT = new BigDecimal(1 - 1);

    private static final Double DEFAULT_CREDIT_SCORE = 1D;
    private static final Double UPDATED_CREDIT_SCORE = 2D;
    private static final Double SMALLER_CREDIT_SCORE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/ex-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExUserRepository exUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExUserMockMvc;

    private ExUser exUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .userLimit(DEFAULT_USER_LIMIT)
            .creditScore(DEFAULT_CREDIT_SCORE);
        return exUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createUpdatedEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .userLimit(UPDATED_USER_LIMIT)
            .creditScore(UPDATED_CREDIT_SCORE);
        return exUser;
    }

    @BeforeEach
    public void initTest() {
        exUser = createEntity(em);
    }

    @Test
    @Transactional
    void createExUser() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();
        // Create the ExUser
        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isCreated());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualByComparingTo(DEFAULT_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(DEFAULT_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void createExUserWithExistingId() throws Exception {
        // Create the ExUser with an existing ID
        exUser.setId(1L);

        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setLogin(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setFirstName(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setEmail(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setPhone(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setAddressLine1(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setCity(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setCountry(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setUserLimit(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExUsers() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].userLimit").value(hasItem(sameNumber(DEFAULT_USER_LIMIT))))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())));
    }

    @Test
    @Transactional
    void getExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get the exUser
        restExUserMockMvc
            .perform(get(ENTITY_API_URL_ID, exUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exUser.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.userLimit").value(sameNumber(DEFAULT_USER_LIMIT)))
            .andExpect(jsonPath("$.creditScore").value(DEFAULT_CREDIT_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    void getExUsersByIdFiltering() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        Long id = exUser.getId();

        defaultExUserShouldBeFound("id.equals=" + id);
        defaultExUserShouldNotBeFound("id.notEquals=" + id);

        defaultExUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.greaterThan=" + id);

        defaultExUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login equals to DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultExUserShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login is not null
        defaultExUserShouldBeFound("login.specified=true");

        // Get all the exUserList where login is null
        defaultExUserShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByLoginContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login contains DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the exUserList where login contains UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login does not contain DEFAULT_LOGIN
        defaultExUserShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the exUserList where login does not contain UPDATED_LOGIN
        defaultExUserShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName is not null
        defaultExUserShouldBeFound("firstName.specified=true");

        // Get all the exUserList where firstName is null
        defaultExUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName contains DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName contains UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName equals to DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName is not null
        defaultExUserShouldBeFound("lastName.specified=true");

        // Get all the exUserList where lastName is null
        defaultExUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName contains DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName contains UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName does not contain UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email equals to DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultExUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email is not null
        defaultExUserShouldBeFound("email.specified=true");

        // Get all the exUserList where email is null
        defaultExUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email contains DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the exUserList where email contains UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email does not contain DEFAULT_EMAIL
        defaultExUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the exUserList where email does not contain UPDATED_EMAIL
        defaultExUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive is not null
        defaultExUserShouldBeFound("isActive.specified=true");

        // Get all the exUserList where isActive is null
        defaultExUserShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone equals to DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultExUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is not null
        defaultExUserShouldBeFound("phone.specified=true");

        // Get all the exUserList where phone is null
        defaultExUserShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone contains DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the exUserList where phone contains UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone does not contain DEFAULT_PHONE
        defaultExUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the exUserList where phone does not contain UPDATED_PHONE
        defaultExUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 is not null
        defaultExUserShouldBeFound("addressLine1.specified=true");

        // Get all the exUserList where addressLine1 is null
        defaultExUserShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 is not null
        defaultExUserShouldBeFound("addressLine2.specified=true");

        // Get all the exUserList where addressLine2 is null
        defaultExUserShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllExUsersByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllExUsersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city equals to DEFAULT_CITY
        defaultExUserShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the exUserList where city equals to UPDATED_CITY
        defaultExUserShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllExUsersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city in DEFAULT_CITY or UPDATED_CITY
        defaultExUserShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the exUserList where city equals to UPDATED_CITY
        defaultExUserShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllExUsersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city is not null
        defaultExUserShouldBeFound("city.specified=true");

        // Get all the exUserList where city is null
        defaultExUserShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByCityContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city contains DEFAULT_CITY
        defaultExUserShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the exUserList where city contains UPDATED_CITY
        defaultExUserShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllExUsersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city does not contain DEFAULT_CITY
        defaultExUserShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the exUserList where city does not contain UPDATED_CITY
        defaultExUserShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllExUsersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country equals to DEFAULT_COUNTRY
        defaultExUserShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country equals to UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllExUsersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultExUserShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the exUserList where country equals to UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllExUsersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country is not null
        defaultExUserShouldBeFound("country.specified=true");

        // Get all the exUserList where country is null
        defaultExUserShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByCountryContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country contains DEFAULT_COUNTRY
        defaultExUserShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country contains UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllExUsersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country does not contain DEFAULT_COUNTRY
        defaultExUserShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country does not contain UPDATED_COUNTRY
        defaultExUserShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit equals to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.equals=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit equals to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.equals=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit in DEFAULT_USER_LIMIT or UPDATED_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.in=" + DEFAULT_USER_LIMIT + "," + UPDATED_USER_LIMIT);

        // Get all the exUserList where userLimit equals to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.in=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is not null
        defaultExUserShouldBeFound("userLimit.specified=true");

        // Get all the exUserList where userLimit is null
        defaultExUserShouldNotBeFound("userLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is greater than or equal to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.greaterThanOrEqual=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is greater than or equal to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.greaterThanOrEqual=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is less than or equal to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.lessThanOrEqual=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is less than or equal to SMALLER_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.lessThanOrEqual=" + SMALLER_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is less than DEFAULT_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.lessThan=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is less than UPDATED_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.lessThan=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByUserLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is greater than DEFAULT_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.greaterThan=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is greater than SMALLER_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.greaterThan=" + SMALLER_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore equals to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.equals=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.equals=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore in DEFAULT_CREDIT_SCORE or UPDATED_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.in=" + DEFAULT_CREDIT_SCORE + "," + UPDATED_CREDIT_SCORE);

        // Get all the exUserList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.in=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is not null
        defaultExUserShouldBeFound("creditScore.specified=true");

        // Get all the exUserList where creditScore is null
        defaultExUserShouldNotBeFound("creditScore.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is greater than or equal to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.greaterThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is greater than or equal to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.greaterThanOrEqual=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is less than or equal to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.lessThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is less than or equal to SMALLER_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.lessThanOrEqual=" + SMALLER_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is less than DEFAULT_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.lessThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is less than UPDATED_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.lessThan=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByCreditScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is greater than DEFAULT_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.greaterThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is greater than SMALLER_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.greaterThan=" + SMALLER_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllExUsersByRelatedUserIsEqualToSomething() throws Exception {
        User relatedUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            exUserRepository.saveAndFlush(exUser);
            relatedUser = UserResourceIT.createEntity(em);
        } else {
            relatedUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(relatedUser);
        em.flush();
        exUser.setRelatedUser(relatedUser);
        exUserRepository.saveAndFlush(exUser);
        Long relatedUserId = relatedUser.getId();

        // Get all the exUserList where relatedUser equals to relatedUserId
        defaultExUserShouldBeFound("relatedUserId.equals=" + relatedUserId);

        // Get all the exUserList where relatedUser equals to (relatedUserId + 1)
        defaultExUserShouldNotBeFound("relatedUserId.equals=" + (relatedUserId + 1));
    }

    @Test
    @Transactional
    void getAllExUsersByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            exUserRepository.saveAndFlush(exUser);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        exUser.addMerchant(merchant);
        exUserRepository.saveAndFlush(exUser);
        Long merchantId = merchant.getId();

        // Get all the exUserList where merchant equals to merchantId
        defaultExUserShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the exUserList where merchant equals to (merchantId + 1)
        defaultExUserShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExUserShouldBeFound(String filter) throws Exception {
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].userLimit").value(hasItem(sameNumber(DEFAULT_USER_LIMIT))))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())));

        // Check, that the count call also returns 1
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExUserShouldNotBeFound(String filter) throws Exception {
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExUser() throws Exception {
        // Get the exUser
        restExUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser
        ExUser updatedExUser = exUserRepository.findById(exUser.getId()).get();
        // Disconnect from session so that the updates on updatedExUser are not directly saved in db
        em.detach(updatedExUser);
        updatedExUser
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .userLimit(UPDATED_USER_LIMIT)
            .creditScore(UPDATED_CREDIT_SCORE);

        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualByComparingTo(UPDATED_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExUserWithPatch() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser using partial update
        ExUser partialUpdatedExUser = new ExUser();
        partialUpdatedExUser.setId(exUser.getId());

        partialUpdatedExUser
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .country(UPDATED_COUNTRY)
            .creditScore(UPDATED_CREDIT_SCORE);

        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualByComparingTo(DEFAULT_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateExUserWithPatch() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser using partial update
        ExUser partialUpdatedExUser = new ExUser();
        partialUpdatedExUser.setId(exUser.getId());

        partialUpdatedExUser
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .userLimit(UPDATED_USER_LIMIT)
            .creditScore(UPDATED_CREDIT_SCORE);

        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualByComparingTo(UPDATED_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeDelete = exUserRepository.findAll().size();

        // Delete the exUser
        restExUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, exUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
