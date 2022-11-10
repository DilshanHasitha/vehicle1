package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.repository.ExpenseRepository;
import com.mycompany.myapp.service.criteria.ExpenseCriteria;
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

/**
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseResourceIT {

    private static final String DEFAULT_EXPENSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPENSE_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPENSE_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXPENSE_LIMIT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseCode(DEFAULT_EXPENSE_CODE)
            .expenseName(DEFAULT_EXPENSE_NAME)
            .expenseLimit(DEFAULT_EXPENSE_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE);
        return expense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseName(UPDATED_EXPENSE_NAME)
            .expenseLimit(UPDATED_EXPENSE_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();
        // Create the Expense
        restExpenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(DEFAULT_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(DEFAULT_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualByComparingTo(DEFAULT_EXPENSE_LIMIT);
        assertThat(testExpense.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createExpenseWithExistingId() throws Exception {
        // Create the Expense with an existing ID
        expense.setId(1L);

        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExpenseCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseCode(null);

        // Create the Expense, which fails.

        restExpenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpenseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseName(null);

        // Create the Expense, which fails.

        restExpenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseName").value(hasItem(DEFAULT_EXPENSE_NAME)))
            .andExpect(jsonPath("$.[*].expenseLimit").value(hasItem(sameNumber(DEFAULT_EXPENSE_LIMIT))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL_ID, expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.expenseCode").value(DEFAULT_EXPENSE_CODE))
            .andExpect(jsonPath("$.expenseName").value(DEFAULT_EXPENSE_NAME))
            .andExpect(jsonPath("$.expenseLimit").value(sameNumber(DEFAULT_EXPENSE_LIMIT)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getExpensesByIdFiltering() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        Long id = expense.getId();

        defaultExpenseShouldBeFound("id.equals=" + id);
        defaultExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode equals to DEFAULT_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.equals=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode equals to UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.equals=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode in DEFAULT_EXPENSE_CODE or UPDATED_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.in=" + DEFAULT_EXPENSE_CODE + "," + UPDATED_EXPENSE_CODE);

        // Get all the expenseList where expenseCode equals to UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.in=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode is not null
        defaultExpenseShouldBeFound("expenseCode.specified=true");

        // Get all the expenseList where expenseCode is null
        defaultExpenseShouldNotBeFound("expenseCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseCodeContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode contains DEFAULT_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.contains=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode contains UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.contains=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseCodeNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode does not contain DEFAULT_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.doesNotContain=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode does not contain UPDATED_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.doesNotContain=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName equals to DEFAULT_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.equals=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName equals to UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.equals=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseNameIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName in DEFAULT_EXPENSE_NAME or UPDATED_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.in=" + DEFAULT_EXPENSE_NAME + "," + UPDATED_EXPENSE_NAME);

        // Get all the expenseList where expenseName equals to UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.in=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName is not null
        defaultExpenseShouldBeFound("expenseName.specified=true");

        // Get all the expenseList where expenseName is null
        defaultExpenseShouldNotBeFound("expenseName.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseNameContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName contains DEFAULT_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.contains=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName contains UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.contains=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseNameNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName does not contain DEFAULT_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.doesNotContain=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName does not contain UPDATED_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.doesNotContain=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit equals to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.equals=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit equals to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.equals=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit in DEFAULT_EXPENSE_LIMIT or UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.in=" + DEFAULT_EXPENSE_LIMIT + "," + UPDATED_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit equals to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.in=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is not null
        defaultExpenseShouldBeFound("expenseLimit.specified=true");

        // Get all the expenseList where expenseLimit is null
        defaultExpenseShouldNotBeFound("expenseLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is greater than or equal to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.greaterThanOrEqual=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is greater than or equal to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.greaterThanOrEqual=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is less than or equal to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.lessThanOrEqual=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is less than or equal to SMALLER_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.lessThanOrEqual=" + SMALLER_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is less than DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.lessThan=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is less than UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.lessThan=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByExpenseLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is greater than DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.greaterThan=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is greater than SMALLER_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.greaterThan=" + SMALLER_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    void getAllExpensesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExpenseShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the expenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExpensesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExpenseShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the expenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExpensesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive is not null
        defaultExpenseShouldBeFound("isActive.specified=true");

        // Get all the expenseList where isActive is null
        defaultExpenseShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseShouldBeFound(String filter) throws Exception {
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseName").value(hasItem(DEFAULT_EXPENSE_NAME)))
            .andExpect(jsonPath("$.[*].expenseLimit").value(hasItem(sameNumber(DEFAULT_EXPENSE_LIMIT))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseShouldNotBeFound(String filter) throws Exception {
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseName(UPDATED_EXPENSE_NAME)
            .expenseLimit(UPDATED_EXPENSE_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);

        restExpenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(UPDATED_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(UPDATED_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualByComparingTo(UPDATED_EXPENSE_LIMIT);
        assertThat(testExpense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseWithPatch() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense using partial update
        Expense partialUpdatedExpense = new Expense();
        partialUpdatedExpense.setId(expense.getId());

        partialUpdatedExpense.expenseLimit(UPDATED_EXPENSE_LIMIT);

        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(DEFAULT_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(DEFAULT_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualByComparingTo(UPDATED_EXPENSE_LIMIT);
        assertThat(testExpense.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateExpenseWithPatch() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense using partial update
        Expense partialUpdatedExpense = new Expense();
        partialUpdatedExpense.setId(expense.getId());

        partialUpdatedExpense
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseName(UPDATED_EXPENSE_NAME)
            .expenseLimit(UPDATED_EXPENSE_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);

        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(UPDATED_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(UPDATED_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualByComparingTo(UPDATED_EXPENSE_LIMIT);
        assertThat(testExpense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();
        expense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, expense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
