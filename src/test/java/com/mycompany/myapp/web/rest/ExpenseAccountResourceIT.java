package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.domain.ExpenseAccount;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.ExpenseAccountRepository;
import com.mycompany.myapp.service.criteria.ExpenseAccountCriteria;
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
 * Integration tests for the {@link ExpenseAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseAccountResourceIT {

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

    private static final String ENTITY_API_URL = "/api/expense-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpenseAccountRepository expenseAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseAccountMockMvc;

    private ExpenseAccount expenseAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccount createEntity(EntityManager em) {
        ExpenseAccount expenseAccount = new ExpenseAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return expenseAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccount createUpdatedEntity(EntityManager em) {
        ExpenseAccount expenseAccount = new ExpenseAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return expenseAccount;
    }

    @BeforeEach
    public void initTest() {
        expenseAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseAccount() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountRepository.findAll().size();
        // Create the ExpenseAccount
        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isCreated());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualByComparingTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void createExpenseAccountWithExistingId() throws Exception {
        // Create the ExpenseAccount with an existing ID
        expenseAccount.setId(1L);

        int databaseSizeBeforeCreate = expenseAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionDate(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionDescription(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionAmountDR(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionAmountCR(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionBalance(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenseAccounts() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));
    }

    @Test
    @Transactional
    void getExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get the expenseAccount
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR)))
            .andExpect(jsonPath("$.transactionAmountCR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR)))
            .andExpect(jsonPath("$.transactionBalance").value(sameNumber(DEFAULT_TRANSACTION_BALANCE)));
    }

    @Test
    @Transactional
    void getExpenseAccountsByIdFiltering() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        Long id = expenseAccount.getId();

        defaultExpenseAccountShouldBeFound("id.equals=" + id);
        defaultExpenseAccountShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is not null
        defaultExpenseAccountShouldBeFound("transactionDate.specified=true");

        // Get all the expenseAccountList where transactionDate is null
        defaultExpenseAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound(
            "transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION
        );

        // Get all the expenseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription is not null
        defaultExpenseAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the expenseAccountList where transactionDescription is null
        defaultExpenseAccountShouldNotBeFound("transactionDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is not null
        defaultExpenseAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the expenseAccountList where transactionAmountDR is null
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is not null
        defaultExpenseAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the expenseAccountList where transactionAmountCR is null
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is not null
        defaultExpenseAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the expenseAccountList where transactionBalance is null
        defaultExpenseAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            expenseAccountRepository.saveAndFlush(expenseAccount);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        expenseAccount.setTransactionType(transactionType);
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the expenseAccountList where transactionType equals to transactionTypeId
        defaultExpenseAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the expenseAccountList where transactionType equals to (transactionTypeId + 1)
        defaultExpenseAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            expenseAccountRepository.saveAndFlush(expenseAccount);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        expenseAccount.setMerchant(merchant);
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long merchantId = merchant.getId();

        // Get all the expenseAccountList where merchant equals to merchantId
        defaultExpenseAccountShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the expenseAccountList where merchant equals to (merchantId + 1)
        defaultExpenseAccountShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllExpenseAccountsByExpenseIsEqualToSomething() throws Exception {
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expenseAccountRepository.saveAndFlush(expenseAccount);
            expense = ExpenseResourceIT.createEntity(em);
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        em.persist(expense);
        em.flush();
        expenseAccount.setExpense(expense);
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long expenseId = expense.getId();

        // Get all the expenseAccountList where expense equals to expenseId
        defaultExpenseAccountShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the expenseAccountList where expense equals to (expenseId + 1)
        defaultExpenseAccountShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseAccountShouldBeFound(String filter) throws Exception {
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));

        // Check, that the count call also returns 1
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseAccountShouldNotBeFound(String filter) throws Exception {
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseAccount() throws Exception {
        // Get the expenseAccount
        restExpenseAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();

        // Update the expenseAccount
        ExpenseAccount updatedExpenseAccount = expenseAccountRepository.findById(expenseAccount.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseAccount are not directly saved in db
        em.detach(updatedExpenseAccount);
        updatedExpenseAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restExpenseAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpenseAccount))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseAccountWithPatch() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();

        // Update the expenseAccount using partial update
        ExpenseAccount partialUpdatedExpenseAccount = new ExpenseAccount();
        partialUpdatedExpenseAccount.setId(expenseAccount.getId());

        partialUpdatedExpenseAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR);

        restExpenseAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseAccount))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualByComparingTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateExpenseAccountWithPatch() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();

        // Update the expenseAccount using partial update
        ExpenseAccount partialUpdatedExpenseAccount = new ExpenseAccount();
        partialUpdatedExpenseAccount.setId(expenseAccount.getId());

        partialUpdatedExpenseAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restExpenseAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseAccount))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();
        expenseAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expenseAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        int databaseSizeBeforeDelete = expenseAccountRepository.findAll().size();

        // Delete the expenseAccount
        restExpenseAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
