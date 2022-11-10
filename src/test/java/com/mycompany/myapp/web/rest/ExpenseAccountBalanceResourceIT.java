package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.domain.ExpenseAccountBalance;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.ExpenseAccountBalanceRepository;
import com.mycompany.myapp.service.criteria.ExpenseAccountBalanceCriteria;
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
 * Integration tests for the {@link ExpenseAccountBalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseAccountBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/expense-account-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseAccountBalanceMockMvc;

    private ExpenseAccountBalance expenseAccountBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccountBalance createEntity(EntityManager em) {
        ExpenseAccountBalance expenseAccountBalance = new ExpenseAccountBalance().balance(DEFAULT_BALANCE);
        return expenseAccountBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccountBalance createUpdatedEntity(EntityManager em) {
        ExpenseAccountBalance expenseAccountBalance = new ExpenseAccountBalance().balance(UPDATED_BALANCE);
        return expenseAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        expenseAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountBalanceRepository.findAll().size();
        // Create the ExpenseAccountBalance
        restExpenseAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isCreated());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void createExpenseAccountBalanceWithExistingId() throws Exception {
        // Create the ExpenseAccountBalance with an existing ID
        expenseAccountBalance.setId(1L);

        int databaseSizeBeforeCreate = expenseAccountBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountBalanceRepository.findAll().size();
        // set the field null
        expenseAccountBalance.setBalance(null);

        // Create the ExpenseAccountBalance, which fails.

        restExpenseAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalances() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));
    }

    @Test
    @Transactional
    void getExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get the expenseAccountBalance
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    void getExpenseAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        Long id = expenseAccountBalance.getId();

        defaultExpenseAccountBalanceShouldBeFound("id.equals=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the expenseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is not null
        defaultExpenseAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the expenseAccountBalanceList where balance is null
        defaultExpenseAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByExpenseIsEqualToSomething() throws Exception {
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
            expense = ExpenseResourceIT.createEntity(em);
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        em.persist(expense);
        em.flush();
        expenseAccountBalance.setExpense(expense);
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long expenseId = expense.getId();

        // Get all the expenseAccountBalanceList where expense equals to expenseId
        defaultExpenseAccountBalanceShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the expenseAccountBalanceList where expense equals to (expenseId + 1)
        defaultExpenseAccountBalanceShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        expenseAccountBalance.setMerchant(merchant);
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long merchantId = merchant.getId();

        // Get all the expenseAccountBalanceList where merchant equals to merchantId
        defaultExpenseAccountBalanceShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the expenseAccountBalanceList where merchant equals to (merchantId + 1)
        defaultExpenseAccountBalanceShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllExpenseAccountBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        expenseAccountBalance.setTransactionType(transactionType);
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the expenseAccountBalanceList where transactionType equals to transactionTypeId
        defaultExpenseAccountBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the expenseAccountBalanceList where transactionType equals to (transactionTypeId + 1)
        defaultExpenseAccountBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseAccountBalanceShouldBeFound(String filter) throws Exception {
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));

        // Check, that the count call also returns 1
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseAccountBalance() throws Exception {
        // Get the expenseAccountBalance
        restExpenseAccountBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();

        // Update the expenseAccountBalance
        ExpenseAccountBalance updatedExpenseAccountBalance = expenseAccountBalanceRepository.findById(expenseAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseAccountBalance are not directly saved in db
        em.detach(updatedExpenseAccountBalance);
        updatedExpenseAccountBalance.balance(UPDATED_BALANCE);

        restExpenseAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpenseAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();

        // Update the expenseAccountBalance using partial update
        ExpenseAccountBalance partialUpdatedExpenseAccountBalance = new ExpenseAccountBalance();
        partialUpdatedExpenseAccountBalance.setId(expenseAccountBalance.getId());

        partialUpdatedExpenseAccountBalance.balance(UPDATED_BALANCE);

        restExpenseAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateExpenseAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();

        // Update the expenseAccountBalance using partial update
        ExpenseAccountBalance partialUpdatedExpenseAccountBalance = new ExpenseAccountBalance();
        partialUpdatedExpenseAccountBalance.setId(expenseAccountBalance.getId());

        partialUpdatedExpenseAccountBalance.balance(UPDATED_BALANCE);

        restExpenseAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();
        expenseAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        int databaseSizeBeforeDelete = expenseAccountBalanceRepository.findAll().size();

        // Delete the expenseAccountBalance
        restExpenseAccountBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseAccountBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
