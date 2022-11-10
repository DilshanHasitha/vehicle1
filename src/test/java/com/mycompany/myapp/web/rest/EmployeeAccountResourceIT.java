package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.EmployeeAccount;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.EmployeeAccountRepository;
import com.mycompany.myapp.service.criteria.EmployeeAccountCriteria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EmployeeAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeAccountResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT_DR = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT_DR = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT_DR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT_CR = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT_CR = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT_CR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TRANSACTION_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/employee-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeAccountMockMvc;

    private EmployeeAccount employeeAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccount createEntity(EntityManager em) {
        EmployeeAccount employeeAccount = new EmployeeAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return employeeAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccount createUpdatedEntity(EntityManager em) {
        EmployeeAccount employeeAccount = new EmployeeAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return employeeAccount;
    }

    @BeforeEach
    public void initTest() {
        employeeAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeAccount() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();
        // Create the EmployeeAccount
        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualByComparingTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void createEmployeeAccountWithExistingId() throws Exception {
        // Create the EmployeeAccount with an existing ID
        employeeAccount.setId(1L);

        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionDate(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionDescription(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionAmountDR(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionAmountCR(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionBalance(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeAccounts() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));
    }

    @Test
    @Transactional
    void getEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get the employeeAccount
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR)))
            .andExpect(jsonPath("$.transactionAmountCR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR)))
            .andExpect(jsonPath("$.transactionBalance").value(sameNumber(DEFAULT_TRANSACTION_BALANCE)));
    }

    @Test
    @Transactional
    void getEmployeeAccountsByIdFiltering() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        Long id = employeeAccount.getId();

        defaultEmployeeAccountShouldBeFound("id.equals=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is not null
        defaultEmployeeAccountShouldBeFound("transactionDate.specified=true");

        // Get all the employeeAccountList where transactionDate is null
        defaultEmployeeAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound(
            "transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION
        );

        // Get all the employeeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription is not null
        defaultEmployeeAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the employeeAccountList where transactionDescription is null
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound(
            "transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR
        );

        // Get all the employeeAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is not null
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the employeeAccountList where transactionAmountDR is null
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound(
            "transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR
        );

        // Get all the employeeAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is not null
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the employeeAccountList where transactionAmountCR is null
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is not null
        defaultEmployeeAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the employeeAccountList where transactionBalance is null
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            employeeAccountRepository.saveAndFlush(employeeAccount);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        employeeAccount.setTransactionType(transactionType);
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the employeeAccountList where transactionType equals to transactionTypeId
        defaultEmployeeAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the employeeAccountList where transactionType equals to (transactionTypeId + 1)
        defaultEmployeeAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            employeeAccountRepository.saveAndFlush(employeeAccount);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        employeeAccount.setMerchant(merchant);
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long merchantId = merchant.getId();

        // Get all the employeeAccountList where merchant equals to merchantId
        defaultEmployeeAccountShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the employeeAccountList where merchant equals to (merchantId + 1)
        defaultEmployeeAccountShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeeAccountsByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employeeAccountRepository.saveAndFlush(employeeAccount);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        employeeAccount.setEmployee(employee);
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long employeeId = employee.getId();

        // Get all the employeeAccountList where employee equals to employeeId
        defaultEmployeeAccountShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employeeAccountList where employee equals to (employeeId + 1)
        defaultEmployeeAccountShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeAccountShouldBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));

        // Check, that the count call also returns 1
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeAccountShouldNotBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeAccount() throws Exception {
        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Update the employeeAccount
        EmployeeAccount updatedEmployeeAccount = employeeAccountRepository.findById(employeeAccount.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeAccount are not directly saved in db
        em.detach(updatedEmployeeAccount);
        updatedEmployeeAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restEmployeeAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAccount))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeAccountWithPatch() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Update the employeeAccount using partial update
        EmployeeAccount partialUpdatedEmployeeAccount = new EmployeeAccount();
        partialUpdatedEmployeeAccount.setId(employeeAccount.getId());

        partialUpdatedEmployeeAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restEmployeeAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeAccount))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeAccountWithPatch() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Update the employeeAccount using partial update
        EmployeeAccount partialUpdatedEmployeeAccount = new EmployeeAccount();
        partialUpdatedEmployeeAccount.setId(employeeAccount.getId());

        partialUpdatedEmployeeAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restEmployeeAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeAccount))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();
        employeeAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        int databaseSizeBeforeDelete = employeeAccountRepository.findAll().size();

        // Delete the employeeAccount
        restEmployeeAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
