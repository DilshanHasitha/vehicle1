package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmployeeAccount;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.service.MerchantService;
import com.mycompany.myapp.service.criteria.MerchantCriteria;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MerchantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MerchantResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MERCHANT_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CREDIT_LIMIT = new BigDecimal(1 - 1);

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

    private static final BigDecimal DEFAULT_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PERCENTAGE = new BigDecimal(1 - 1);

    private static final Double DEFAULT_CREDIT_SCORE = 1D;
    private static final Double UPDATED_CREDIT_SCORE = 2D;
    private static final Double SMALLER_CREDIT_SCORE = 1D - 1D;

    private static final String DEFAULT_EMAIL = "g@YG#s.e:8";
    private static final String UPDATED_EMAIL = "(K1@w.L2f";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final Integer DEFAULT_LEAD_TIME = 1;
    private static final Integer UPDATED_LEAD_TIME = 2;
    private static final Integer SMALLER_LEAD_TIME = 1 - 1;

    private static final Boolean DEFAULT_IS_SAND_BOX = false;
    private static final Boolean UPDATED_IS_SAND_BOX = true;

    private static final String DEFAULT_STORE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_STORE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_SECONDARY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_STORE_SECONDARY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/merchants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantRepository merchantRepositoryMock;

    @Mock
    private MerchantService merchantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .code(DEFAULT_CODE)
            .merchantSecret(DEFAULT_MERCHANT_SECRET)
            .name(DEFAULT_NAME)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .percentage(DEFAULT_PERCENTAGE)
            .creditScore(DEFAULT_CREDIT_SCORE)
            .email(DEFAULT_EMAIL)
            .rating(DEFAULT_RATING)
            .leadTime(DEFAULT_LEAD_TIME)
            .isSandBox(DEFAULT_IS_SAND_BOX)
            .storeDescription(DEFAULT_STORE_DESCRIPTION)
            .storeSecondaryDescription(DEFAULT_STORE_SECONDARY_DESCRIPTION);
        return merchant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createUpdatedEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .code(UPDATED_CODE)
            .merchantSecret(UPDATED_MERCHANT_SECRET)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .percentage(UPDATED_PERCENTAGE)
            .creditScore(UPDATED_CREDIT_SCORE)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .leadTime(UPDATED_LEAD_TIME)
            .isSandBox(UPDATED_IS_SAND_BOX)
            .storeDescription(UPDATED_STORE_DESCRIPTION)
            .storeSecondaryDescription(UPDATED_STORE_SECONDARY_DESCRIPTION);
        return merchant;
    }

    @BeforeEach
    public void initTest() {
        merchant = createEntity(em);
    }

    @Test
    @Transactional
    void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();
        // Create the Merchant
        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMerchant.getMerchantSecret()).isEqualTo(DEFAULT_MERCHANT_SECRET);
        assertThat(testMerchant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchant.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testMerchant.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testMerchant.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMerchant.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testMerchant.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testMerchant.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMerchant.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMerchant.getPercentage()).isEqualByComparingTo(DEFAULT_PERCENTAGE);
        assertThat(testMerchant.getCreditScore()).isEqualTo(DEFAULT_CREDIT_SCORE);
        assertThat(testMerchant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMerchant.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testMerchant.getLeadTime()).isEqualTo(DEFAULT_LEAD_TIME);
        assertThat(testMerchant.getIsSandBox()).isEqualTo(DEFAULT_IS_SAND_BOX);
        assertThat(testMerchant.getStoreDescription()).isEqualTo(DEFAULT_STORE_DESCRIPTION);
        assertThat(testMerchant.getStoreSecondaryDescription()).isEqualTo(DEFAULT_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void createMerchantWithExistingId() throws Exception {
        // Create the Merchant with an existing ID
        merchant.setId(1L);

        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setCode(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMerchantSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setMerchantSecret(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setName(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setCreditLimit(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setPhone(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setAddressLine1(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setCity(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setCountry(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setPercentage(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setEmail(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].merchantSecret").value(hasItem(DEFAULT_MERCHANT_SECRET)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(sameNumber(DEFAULT_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].isSandBox").value(hasItem(DEFAULT_IS_SAND_BOX.booleanValue())))
            .andExpect(jsonPath("$.[*].storeDescription").value(hasItem(DEFAULT_STORE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].storeSecondaryDescription").value(hasItem(DEFAULT_STORE_SECONDARY_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMerchantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(merchantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMerchantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(merchantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMerchantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(merchantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMerchantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(merchantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL_ID, merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.merchantSecret").value(DEFAULT_MERCHANT_SECRET))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.creditLimit").value(sameNumber(DEFAULT_CREDIT_LIMIT)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.percentage").value(sameNumber(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.creditScore").value(DEFAULT_CREDIT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.leadTime").value(DEFAULT_LEAD_TIME))
            .andExpect(jsonPath("$.isSandBox").value(DEFAULT_IS_SAND_BOX.booleanValue()))
            .andExpect(jsonPath("$.storeDescription").value(DEFAULT_STORE_DESCRIPTION))
            .andExpect(jsonPath("$.storeSecondaryDescription").value(DEFAULT_STORE_SECONDARY_DESCRIPTION));
    }

    @Test
    @Transactional
    void getMerchantsByIdFiltering() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        Long id = merchant.getId();

        defaultMerchantShouldBeFound("id.equals=" + id);
        defaultMerchantShouldNotBeFound("id.notEquals=" + id);

        defaultMerchantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMerchantShouldNotBeFound("id.greaterThan=" + id);

        defaultMerchantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMerchantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMerchantsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where code equals to DEFAULT_CODE
        defaultMerchantShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the merchantList where code equals to UPDATED_CODE
        defaultMerchantShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMerchantShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the merchantList where code equals to UPDATED_CODE
        defaultMerchantShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where code is not null
        defaultMerchantShouldBeFound("code.specified=true");

        // Get all the merchantList where code is null
        defaultMerchantShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByCodeContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where code contains DEFAULT_CODE
        defaultMerchantShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the merchantList where code contains UPDATED_CODE
        defaultMerchantShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where code does not contain DEFAULT_CODE
        defaultMerchantShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the merchantList where code does not contain UPDATED_CODE
        defaultMerchantShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByMerchantSecretIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where merchantSecret equals to DEFAULT_MERCHANT_SECRET
        defaultMerchantShouldBeFound("merchantSecret.equals=" + DEFAULT_MERCHANT_SECRET);

        // Get all the merchantList where merchantSecret equals to UPDATED_MERCHANT_SECRET
        defaultMerchantShouldNotBeFound("merchantSecret.equals=" + UPDATED_MERCHANT_SECRET);
    }

    @Test
    @Transactional
    void getAllMerchantsByMerchantSecretIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where merchantSecret in DEFAULT_MERCHANT_SECRET or UPDATED_MERCHANT_SECRET
        defaultMerchantShouldBeFound("merchantSecret.in=" + DEFAULT_MERCHANT_SECRET + "," + UPDATED_MERCHANT_SECRET);

        // Get all the merchantList where merchantSecret equals to UPDATED_MERCHANT_SECRET
        defaultMerchantShouldNotBeFound("merchantSecret.in=" + UPDATED_MERCHANT_SECRET);
    }

    @Test
    @Transactional
    void getAllMerchantsByMerchantSecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where merchantSecret is not null
        defaultMerchantShouldBeFound("merchantSecret.specified=true");

        // Get all the merchantList where merchantSecret is null
        defaultMerchantShouldNotBeFound("merchantSecret.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByMerchantSecretContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where merchantSecret contains DEFAULT_MERCHANT_SECRET
        defaultMerchantShouldBeFound("merchantSecret.contains=" + DEFAULT_MERCHANT_SECRET);

        // Get all the merchantList where merchantSecret contains UPDATED_MERCHANT_SECRET
        defaultMerchantShouldNotBeFound("merchantSecret.contains=" + UPDATED_MERCHANT_SECRET);
    }

    @Test
    @Transactional
    void getAllMerchantsByMerchantSecretNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where merchantSecret does not contain DEFAULT_MERCHANT_SECRET
        defaultMerchantShouldNotBeFound("merchantSecret.doesNotContain=" + DEFAULT_MERCHANT_SECRET);

        // Get all the merchantList where merchantSecret does not contain UPDATED_MERCHANT_SECRET
        defaultMerchantShouldBeFound("merchantSecret.doesNotContain=" + UPDATED_MERCHANT_SECRET);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name equals to DEFAULT_NAME
        defaultMerchantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the merchantList where name equals to UPDATED_NAME
        defaultMerchantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMerchantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the merchantList where name equals to UPDATED_NAME
        defaultMerchantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name is not null
        defaultMerchantShouldBeFound("name.specified=true");

        // Get all the merchantList where name is null
        defaultMerchantShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByNameContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name contains DEFAULT_NAME
        defaultMerchantShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the merchantList where name contains UPDATED_NAME
        defaultMerchantShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name does not contain DEFAULT_NAME
        defaultMerchantShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the merchantList where name does not contain UPDATED_NAME
        defaultMerchantShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit equals to DEFAULT_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.equals=" + DEFAULT_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.equals=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit in DEFAULT_CREDIT_LIMIT or UPDATED_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.in=" + DEFAULT_CREDIT_LIMIT + "," + UPDATED_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.in=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit is not null
        defaultMerchantShouldBeFound("creditLimit.specified=true");

        // Get all the merchantList where creditLimit is null
        defaultMerchantShouldNotBeFound("creditLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit is greater than or equal to DEFAULT_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.greaterThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit is greater than or equal to UPDATED_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.greaterThanOrEqual=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit is less than or equal to DEFAULT_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.lessThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit is less than or equal to SMALLER_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.lessThanOrEqual=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit is less than DEFAULT_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.lessThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit is less than UPDATED_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.lessThan=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditLimit is greater than DEFAULT_CREDIT_LIMIT
        defaultMerchantShouldNotBeFound("creditLimit.greaterThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the merchantList where creditLimit is greater than SMALLER_CREDIT_LIMIT
        defaultMerchantShouldBeFound("creditLimit.greaterThan=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMerchantShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the merchantList where isActive equals to UPDATED_IS_ACTIVE
        defaultMerchantShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMerchantShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the merchantList where isActive equals to UPDATED_IS_ACTIVE
        defaultMerchantShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isActive is not null
        defaultMerchantShouldBeFound("isActive.specified=true");

        // Get all the merchantList where isActive is null
        defaultMerchantShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where phone equals to DEFAULT_PHONE
        defaultMerchantShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the merchantList where phone equals to UPDATED_PHONE
        defaultMerchantShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultMerchantShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the merchantList where phone equals to UPDATED_PHONE
        defaultMerchantShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where phone is not null
        defaultMerchantShouldBeFound("phone.specified=true");

        // Get all the merchantList where phone is null
        defaultMerchantShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where phone contains DEFAULT_PHONE
        defaultMerchantShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the merchantList where phone contains UPDATED_PHONE
        defaultMerchantShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where phone does not contain DEFAULT_PHONE
        defaultMerchantShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the merchantList where phone does not contain UPDATED_PHONE
        defaultMerchantShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultMerchantShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the merchantList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultMerchantShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultMerchantShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the merchantList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultMerchantShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine1 is not null
        defaultMerchantShouldBeFound("addressLine1.specified=true");

        // Get all the merchantList where addressLine1 is null
        defaultMerchantShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultMerchantShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the merchantList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultMerchantShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultMerchantShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the merchantList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultMerchantShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultMerchantShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the merchantList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultMerchantShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultMerchantShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the merchantList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultMerchantShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine2 is not null
        defaultMerchantShouldBeFound("addressLine2.specified=true");

        // Get all the merchantList where addressLine2 is null
        defaultMerchantShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultMerchantShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the merchantList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultMerchantShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllMerchantsByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultMerchantShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the merchantList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultMerchantShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllMerchantsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where city equals to DEFAULT_CITY
        defaultMerchantShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the merchantList where city equals to UPDATED_CITY
        defaultMerchantShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where city in DEFAULT_CITY or UPDATED_CITY
        defaultMerchantShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the merchantList where city equals to UPDATED_CITY
        defaultMerchantShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where city is not null
        defaultMerchantShouldBeFound("city.specified=true");

        // Get all the merchantList where city is null
        defaultMerchantShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByCityContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where city contains DEFAULT_CITY
        defaultMerchantShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the merchantList where city contains UPDATED_CITY
        defaultMerchantShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where city does not contain DEFAULT_CITY
        defaultMerchantShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the merchantList where city does not contain UPDATED_CITY
        defaultMerchantShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where country equals to DEFAULT_COUNTRY
        defaultMerchantShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the merchantList where country equals to UPDATED_COUNTRY
        defaultMerchantShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultMerchantShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the merchantList where country equals to UPDATED_COUNTRY
        defaultMerchantShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where country is not null
        defaultMerchantShouldBeFound("country.specified=true");

        // Get all the merchantList where country is null
        defaultMerchantShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByCountryContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where country contains DEFAULT_COUNTRY
        defaultMerchantShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the merchantList where country contains UPDATED_COUNTRY
        defaultMerchantShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllMerchantsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where country does not contain DEFAULT_COUNTRY
        defaultMerchantShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the merchantList where country does not contain UPDATED_COUNTRY
        defaultMerchantShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage equals to DEFAULT_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the merchantList where percentage equals to UPDATED_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the merchantList where percentage equals to UPDATED_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage is not null
        defaultMerchantShouldBeFound("percentage.specified=true");

        // Get all the merchantList where percentage is null
        defaultMerchantShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage is greater than or equal to DEFAULT_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.greaterThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the merchantList where percentage is greater than or equal to UPDATED_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.greaterThanOrEqual=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage is less than or equal to DEFAULT_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.lessThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the merchantList where percentage is less than or equal to SMALLER_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.lessThanOrEqual=" + SMALLER_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage is less than DEFAULT_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.lessThan=" + DEFAULT_PERCENTAGE);

        // Get all the merchantList where percentage is less than UPDATED_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.lessThan=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where percentage is greater than DEFAULT_PERCENTAGE
        defaultMerchantShouldNotBeFound("percentage.greaterThan=" + DEFAULT_PERCENTAGE);

        // Get all the merchantList where percentage is greater than SMALLER_PERCENTAGE
        defaultMerchantShouldBeFound("percentage.greaterThan=" + SMALLER_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore equals to DEFAULT_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.equals=" + DEFAULT_CREDIT_SCORE);

        // Get all the merchantList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.equals=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore in DEFAULT_CREDIT_SCORE or UPDATED_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.in=" + DEFAULT_CREDIT_SCORE + "," + UPDATED_CREDIT_SCORE);

        // Get all the merchantList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.in=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore is not null
        defaultMerchantShouldBeFound("creditScore.specified=true");

        // Get all the merchantList where creditScore is null
        defaultMerchantShouldNotBeFound("creditScore.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore is greater than or equal to DEFAULT_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.greaterThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the merchantList where creditScore is greater than or equal to UPDATED_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.greaterThanOrEqual=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore is less than or equal to DEFAULT_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.lessThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the merchantList where creditScore is less than or equal to SMALLER_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.lessThanOrEqual=" + SMALLER_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore is less than DEFAULT_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.lessThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the merchantList where creditScore is less than UPDATED_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.lessThan=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByCreditScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where creditScore is greater than DEFAULT_CREDIT_SCORE
        defaultMerchantShouldNotBeFound("creditScore.greaterThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the merchantList where creditScore is greater than SMALLER_CREDIT_SCORE
        defaultMerchantShouldBeFound("creditScore.greaterThan=" + SMALLER_CREDIT_SCORE);
    }

    @Test
    @Transactional
    void getAllMerchantsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where email equals to DEFAULT_EMAIL
        defaultMerchantShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the merchantList where email equals to UPDATED_EMAIL
        defaultMerchantShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMerchantsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultMerchantShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the merchantList where email equals to UPDATED_EMAIL
        defaultMerchantShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMerchantsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where email is not null
        defaultMerchantShouldBeFound("email.specified=true");

        // Get all the merchantList where email is null
        defaultMerchantShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByEmailContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where email contains DEFAULT_EMAIL
        defaultMerchantShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the merchantList where email contains UPDATED_EMAIL
        defaultMerchantShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMerchantsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where email does not contain DEFAULT_EMAIL
        defaultMerchantShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the merchantList where email does not contain UPDATED_EMAIL
        defaultMerchantShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating equals to DEFAULT_RATING
        defaultMerchantShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the merchantList where rating equals to UPDATED_RATING
        defaultMerchantShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultMerchantShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the merchantList where rating equals to UPDATED_RATING
        defaultMerchantShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating is not null
        defaultMerchantShouldBeFound("rating.specified=true");

        // Get all the merchantList where rating is null
        defaultMerchantShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating is greater than or equal to DEFAULT_RATING
        defaultMerchantShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the merchantList where rating is greater than or equal to UPDATED_RATING
        defaultMerchantShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating is less than or equal to DEFAULT_RATING
        defaultMerchantShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the merchantList where rating is less than or equal to SMALLER_RATING
        defaultMerchantShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating is less than DEFAULT_RATING
        defaultMerchantShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the merchantList where rating is less than UPDATED_RATING
        defaultMerchantShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where rating is greater than DEFAULT_RATING
        defaultMerchantShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the merchantList where rating is greater than SMALLER_RATING
        defaultMerchantShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime equals to DEFAULT_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.equals=" + DEFAULT_LEAD_TIME);

        // Get all the merchantList where leadTime equals to UPDATED_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.equals=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime in DEFAULT_LEAD_TIME or UPDATED_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.in=" + DEFAULT_LEAD_TIME + "," + UPDATED_LEAD_TIME);

        // Get all the merchantList where leadTime equals to UPDATED_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.in=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime is not null
        defaultMerchantShouldBeFound("leadTime.specified=true");

        // Get all the merchantList where leadTime is null
        defaultMerchantShouldNotBeFound("leadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime is greater than or equal to DEFAULT_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.greaterThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the merchantList where leadTime is greater than or equal to UPDATED_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.greaterThanOrEqual=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime is less than or equal to DEFAULT_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.lessThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the merchantList where leadTime is less than or equal to SMALLER_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.lessThanOrEqual=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime is less than DEFAULT_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.lessThan=" + DEFAULT_LEAD_TIME);

        // Get all the merchantList where leadTime is less than UPDATED_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.lessThan=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByLeadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where leadTime is greater than DEFAULT_LEAD_TIME
        defaultMerchantShouldNotBeFound("leadTime.greaterThan=" + DEFAULT_LEAD_TIME);

        // Get all the merchantList where leadTime is greater than SMALLER_LEAD_TIME
        defaultMerchantShouldBeFound("leadTime.greaterThan=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsSandBoxIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isSandBox equals to DEFAULT_IS_SAND_BOX
        defaultMerchantShouldBeFound("isSandBox.equals=" + DEFAULT_IS_SAND_BOX);

        // Get all the merchantList where isSandBox equals to UPDATED_IS_SAND_BOX
        defaultMerchantShouldNotBeFound("isSandBox.equals=" + UPDATED_IS_SAND_BOX);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsSandBoxIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isSandBox in DEFAULT_IS_SAND_BOX or UPDATED_IS_SAND_BOX
        defaultMerchantShouldBeFound("isSandBox.in=" + DEFAULT_IS_SAND_BOX + "," + UPDATED_IS_SAND_BOX);

        // Get all the merchantList where isSandBox equals to UPDATED_IS_SAND_BOX
        defaultMerchantShouldNotBeFound("isSandBox.in=" + UPDATED_IS_SAND_BOX);
    }

    @Test
    @Transactional
    void getAllMerchantsByIsSandBoxIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where isSandBox is not null
        defaultMerchantShouldBeFound("isSandBox.specified=true");

        // Get all the merchantList where isSandBox is null
        defaultMerchantShouldNotBeFound("isSandBox.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeDescription equals to DEFAULT_STORE_DESCRIPTION
        defaultMerchantShouldBeFound("storeDescription.equals=" + DEFAULT_STORE_DESCRIPTION);

        // Get all the merchantList where storeDescription equals to UPDATED_STORE_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeDescription.equals=" + UPDATED_STORE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeDescription in DEFAULT_STORE_DESCRIPTION or UPDATED_STORE_DESCRIPTION
        defaultMerchantShouldBeFound("storeDescription.in=" + DEFAULT_STORE_DESCRIPTION + "," + UPDATED_STORE_DESCRIPTION);

        // Get all the merchantList where storeDescription equals to UPDATED_STORE_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeDescription.in=" + UPDATED_STORE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeDescription is not null
        defaultMerchantShouldBeFound("storeDescription.specified=true");

        // Get all the merchantList where storeDescription is null
        defaultMerchantShouldNotBeFound("storeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreDescriptionContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeDescription contains DEFAULT_STORE_DESCRIPTION
        defaultMerchantShouldBeFound("storeDescription.contains=" + DEFAULT_STORE_DESCRIPTION);

        // Get all the merchantList where storeDescription contains UPDATED_STORE_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeDescription.contains=" + UPDATED_STORE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeDescription does not contain DEFAULT_STORE_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeDescription.doesNotContain=" + DEFAULT_STORE_DESCRIPTION);

        // Get all the merchantList where storeDescription does not contain UPDATED_STORE_DESCRIPTION
        defaultMerchantShouldBeFound("storeDescription.doesNotContain=" + UPDATED_STORE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreSecondaryDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeSecondaryDescription equals to DEFAULT_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldBeFound("storeSecondaryDescription.equals=" + DEFAULT_STORE_SECONDARY_DESCRIPTION);

        // Get all the merchantList where storeSecondaryDescription equals to UPDATED_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeSecondaryDescription.equals=" + UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreSecondaryDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeSecondaryDescription in DEFAULT_STORE_SECONDARY_DESCRIPTION or UPDATED_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldBeFound(
            "storeSecondaryDescription.in=" + DEFAULT_STORE_SECONDARY_DESCRIPTION + "," + UPDATED_STORE_SECONDARY_DESCRIPTION
        );

        // Get all the merchantList where storeSecondaryDescription equals to UPDATED_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeSecondaryDescription.in=" + UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreSecondaryDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeSecondaryDescription is not null
        defaultMerchantShouldBeFound("storeSecondaryDescription.specified=true");

        // Get all the merchantList where storeSecondaryDescription is null
        defaultMerchantShouldNotBeFound("storeSecondaryDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreSecondaryDescriptionContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeSecondaryDescription contains DEFAULT_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldBeFound("storeSecondaryDescription.contains=" + DEFAULT_STORE_SECONDARY_DESCRIPTION);

        // Get all the merchantList where storeSecondaryDescription contains UPDATED_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeSecondaryDescription.contains=" + UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByStoreSecondaryDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where storeSecondaryDescription does not contain DEFAULT_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldNotBeFound("storeSecondaryDescription.doesNotContain=" + DEFAULT_STORE_SECONDARY_DESCRIPTION);

        // Get all the merchantList where storeSecondaryDescription does not contain UPDATED_STORE_SECONDARY_DESCRIPTION
        defaultMerchantShouldBeFound("storeSecondaryDescription.doesNotContain=" + UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMerchantsByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            merchantRepository.saveAndFlush(merchant);
            vehicle = VehicleResourceIT.createEntity(em);
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        merchant.addVehicle(vehicle);
        merchantRepository.saveAndFlush(merchant);
        Long vehicleId = vehicle.getId();

        // Get all the merchantList where vehicle equals to vehicleId
        defaultMerchantShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the merchantList where vehicle equals to (vehicleId + 1)
        defaultMerchantShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }

    @Test
    @Transactional
    void getAllMerchantsByImagesIsEqualToSomething() throws Exception {
        Images images;
        if (TestUtil.findAll(em, Images.class).isEmpty()) {
            merchantRepository.saveAndFlush(merchant);
            images = ImagesResourceIT.createEntity(em);
        } else {
            images = TestUtil.findAll(em, Images.class).get(0);
        }
        em.persist(images);
        em.flush();
        merchant.setImages(images);
        merchantRepository.saveAndFlush(merchant);
        Long imagesId = images.getId();

        // Get all the merchantList where images equals to imagesId
        defaultMerchantShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the merchantList where images equals to (imagesId + 1)
        defaultMerchantShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }

    @Test
    @Transactional
    void getAllMerchantsByExUserIsEqualToSomething() throws Exception {
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            merchantRepository.saveAndFlush(merchant);
            exUser = ExUserResourceIT.createEntity(em);
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(exUser);
        em.flush();
        merchant.addExUser(exUser);
        merchantRepository.saveAndFlush(merchant);
        Long exUserId = exUser.getId();

        // Get all the merchantList where exUser equals to exUserId
        defaultMerchantShouldBeFound("exUserId.equals=" + exUserId);

        // Get all the merchantList where exUser equals to (exUserId + 1)
        defaultMerchantShouldNotBeFound("exUserId.equals=" + (exUserId + 1));
    }

    @Test
    @Transactional
    void getAllMerchantsByEmployeeAccountIsEqualToSomething() throws Exception {
        EmployeeAccount employeeAccount;
        if (TestUtil.findAll(em, EmployeeAccount.class).isEmpty()) {
            merchantRepository.saveAndFlush(merchant);
            employeeAccount = EmployeeAccountResourceIT.createEntity(em);
        } else {
            employeeAccount = TestUtil.findAll(em, EmployeeAccount.class).get(0);
        }
        em.persist(employeeAccount);
        em.flush();
        merchant.addEmployeeAccount(employeeAccount);
        merchantRepository.saveAndFlush(merchant);
        Long employeeAccountId = employeeAccount.getId();

        // Get all the merchantList where employeeAccount equals to employeeAccountId
        defaultMerchantShouldBeFound("employeeAccountId.equals=" + employeeAccountId);

        // Get all the merchantList where employeeAccount equals to (employeeAccountId + 1)
        defaultMerchantShouldNotBeFound("employeeAccountId.equals=" + (employeeAccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMerchantShouldBeFound(String filter) throws Exception {
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].merchantSecret").value(hasItem(DEFAULT_MERCHANT_SECRET)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(sameNumber(DEFAULT_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].isSandBox").value(hasItem(DEFAULT_IS_SAND_BOX.booleanValue())))
            .andExpect(jsonPath("$.[*].storeDescription").value(hasItem(DEFAULT_STORE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].storeSecondaryDescription").value(hasItem(DEFAULT_STORE_SECONDARY_DESCRIPTION)));

        // Check, that the count call also returns 1
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMerchantShouldNotBeFound(String filter) throws Exception {
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).get();
        // Disconnect from session so that the updates on updatedMerchant are not directly saved in db
        em.detach(updatedMerchant);
        updatedMerchant
            .code(UPDATED_CODE)
            .merchantSecret(UPDATED_MERCHANT_SECRET)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .percentage(UPDATED_PERCENTAGE)
            .creditScore(UPDATED_CREDIT_SCORE)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .leadTime(UPDATED_LEAD_TIME)
            .isSandBox(UPDATED_IS_SAND_BOX)
            .storeDescription(UPDATED_STORE_DESCRIPTION)
            .storeSecondaryDescription(UPDATED_STORE_SECONDARY_DESCRIPTION);

        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMerchant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMerchant.getMerchantSecret()).isEqualTo(UPDATED_MERCHANT_SECRET);
        assertThat(testMerchant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchant.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testMerchant.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testMerchant.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMerchant.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testMerchant.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testMerchant.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMerchant.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMerchant.getPercentage()).isEqualByComparingTo(UPDATED_PERCENTAGE);
        assertThat(testMerchant.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testMerchant.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testMerchant.getIsSandBox()).isEqualTo(UPDATED_IS_SAND_BOX);
        assertThat(testMerchant.getStoreDescription()).isEqualTo(UPDATED_STORE_DESCRIPTION);
        assertThat(testMerchant.getStoreSecondaryDescription()).isEqualTo(UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant
            .code(UPDATED_CODE)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .creditScore(UPDATED_CREDIT_SCORE)
            .email(UPDATED_EMAIL)
            .leadTime(UPDATED_LEAD_TIME)
            .isSandBox(UPDATED_IS_SAND_BOX)
            .storeDescription(UPDATED_STORE_DESCRIPTION)
            .storeSecondaryDescription(UPDATED_STORE_SECONDARY_DESCRIPTION);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMerchant.getMerchantSecret()).isEqualTo(DEFAULT_MERCHANT_SECRET);
        assertThat(testMerchant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchant.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testMerchant.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testMerchant.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMerchant.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testMerchant.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testMerchant.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMerchant.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMerchant.getPercentage()).isEqualByComparingTo(DEFAULT_PERCENTAGE);
        assertThat(testMerchant.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testMerchant.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testMerchant.getIsSandBox()).isEqualTo(UPDATED_IS_SAND_BOX);
        assertThat(testMerchant.getStoreDescription()).isEqualTo(UPDATED_STORE_DESCRIPTION);
        assertThat(testMerchant.getStoreSecondaryDescription()).isEqualTo(UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant
            .code(UPDATED_CODE)
            .merchantSecret(UPDATED_MERCHANT_SECRET)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .percentage(UPDATED_PERCENTAGE)
            .creditScore(UPDATED_CREDIT_SCORE)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .leadTime(UPDATED_LEAD_TIME)
            .isSandBox(UPDATED_IS_SAND_BOX)
            .storeDescription(UPDATED_STORE_DESCRIPTION)
            .storeSecondaryDescription(UPDATED_STORE_SECONDARY_DESCRIPTION);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMerchant.getMerchantSecret()).isEqualTo(UPDATED_MERCHANT_SECRET);
        assertThat(testMerchant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchant.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testMerchant.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testMerchant.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMerchant.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testMerchant.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testMerchant.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMerchant.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMerchant.getPercentage()).isEqualByComparingTo(UPDATED_PERCENTAGE);
        assertThat(testMerchant.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testMerchant.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testMerchant.getIsSandBox()).isEqualTo(UPDATED_IS_SAND_BOX);
        assertThat(testMerchant.getStoreDescription()).isEqualTo(UPDATED_STORE_DESCRIPTION);
        assertThat(testMerchant.getStoreSecondaryDescription()).isEqualTo(UPDATED_STORE_SECONDARY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, merchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Delete the merchant
        restMerchantMockMvc
            .perform(delete(ENTITY_API_URL_ID, merchant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
