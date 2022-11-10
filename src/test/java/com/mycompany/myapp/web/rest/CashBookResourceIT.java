package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.CashBookRepository;
import com.mycompany.myapp.service.criteria.CashBookCriteria;
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
 * Integration tests for the {@link CashBookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CashBookResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cash-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CashBookRepository cashBookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashBookMockMvc;

    private CashBook cashBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return cashBook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createUpdatedEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return cashBook;
    }

    @BeforeEach
    public void initTest() {
        cashBook = createEntity(em);
    }

    @Test
    @Transactional
    void createCashBook() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();
        // Create the CashBook
        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isCreated());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate + 1);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualByComparingTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void createCashBookWithExistingId() throws Exception {
        // Create the CashBook with an existing ID
        cashBook.setId(1L);

        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionDate(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionDescription(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionAmountDR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionAmountCR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionBalance(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCashBooks() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));
    }

    @Test
    @Transactional
    void getCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get the cashBook
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL_ID, cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashBook.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR)))
            .andExpect(jsonPath("$.transactionAmountCR").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR)))
            .andExpect(jsonPath("$.transactionBalance").value(sameNumber(DEFAULT_TRANSACTION_BALANCE)));
    }

    @Test
    @Transactional
    void getCashBooksByIdFiltering() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        Long id = cashBook.getId();

        defaultCashBookShouldBeFound("id.equals=" + id);
        defaultCashBookShouldNotBeFound("id.notEquals=" + id);

        defaultCashBookShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashBookShouldNotBeFound("id.greaterThan=" + id);

        defaultCashBookShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashBookShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is not null
        defaultCashBookShouldBeFound("transactionDate.specified=true");

        // Get all the cashBookList where transactionDate is null
        defaultCashBookShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound(
            "transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION
        );

        // Get all the cashBookList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription is not null
        defaultCashBookShouldBeFound("transactionDescription.specified=true");

        // Get all the cashBookList where transactionDescription is null
        defaultCashBookShouldNotBeFound("transactionDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is not null
        defaultCashBookShouldBeFound("transactionAmountDR.specified=true");

        // Get all the cashBookList where transactionAmountDR is null
        defaultCashBookShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is not null
        defaultCashBookShouldBeFound("transactionAmountCR.specified=true");

        // Get all the cashBookList where transactionAmountCR is null
        defaultCashBookShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is not null
        defaultCashBookShouldBeFound("transactionBalance.specified=true");

        // Get all the cashBookList where transactionBalance is null
        defaultCashBookShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBooksByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        cashBook.setMerchant(merchant);
        cashBookRepository.saveAndFlush(cashBook);
        Long merchantId = merchant.getId();

        // Get all the cashBookList where merchant equals to merchantId
        defaultCashBookShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the cashBookList where merchant equals to (merchantId + 1)
        defaultCashBookShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        cashBook.setTransactionType(transactionType);
        cashBookRepository.saveAndFlush(cashBook);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashBookList where transactionType equals to transactionTypeId
        defaultCashBookShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashBookList where transactionType equals to (transactionTypeId + 1)
        defaultCashBookShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookShouldBeFound(String filter) throws Exception {
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_DR))))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT_CR))))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(sameNumber(DEFAULT_TRANSACTION_BALANCE))));

        // Check, that the count call also returns 1
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookShouldNotBeFound(String filter) throws Exception {
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCashBook() throws Exception {
        // Get the cashBook
        restCashBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook
        CashBook updatedCashBook = cashBookRepository.findById(cashBook.getId()).get();
        // Disconnect from session so that the updates on updatedCashBook are not directly saved in db
        em.detach(updatedCashBook);
        updatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashBookWithPatch() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook using partial update
        CashBook partialUpdatedCashBook = new CashBook();
        partialUpdatedCashBook.setId(cashBook.getId());

        partialUpdatedCashBook.transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR);

        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualByComparingTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateCashBookWithPatch() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook using partial update
        CashBook partialUpdatedCashBook = new CashBook();
        partialUpdatedCashBook.setId(cashBook.getId());

        partialUpdatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualByComparingTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();
        cashBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        int databaseSizeBeforeDelete = cashBookRepository.findAll().size();

        // Delete the cashBook
        restCashBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
