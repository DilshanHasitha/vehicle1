package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CashBookBalance;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.CashBookBalanceRepository;
import com.mycompany.myapp.service.criteria.CashBookBalanceCriteria;
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
 * Integration tests for the {@link CashBookBalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CashBookBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/cash-book-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CashBookBalanceRepository cashBookBalanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashBookBalanceMockMvc;

    private CashBookBalance cashBookBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookBalance createEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance().balance(DEFAULT_BALANCE);
        return cashBookBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookBalance createUpdatedEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance().balance(UPDATED_BALANCE);
        return cashBookBalance;
    }

    @BeforeEach
    public void initTest() {
        cashBookBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createCashBookBalance() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();
        // Create the CashBookBalance
        restCashBookBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isCreated());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void createCashBookBalanceWithExistingId() throws Exception {
        // Create the CashBookBalance with an existing ID
        cashBookBalance.setId(1L);

        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookBalanceRepository.findAll().size();
        // set the field null
        cashBookBalance.setBalance(null);

        // Create the CashBookBalance, which fails.

        restCashBookBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCashBookBalances() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));
    }

    @Test
    @Transactional
    void getCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get the cashBookBalance
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, cashBookBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashBookBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    void getCashBookBalancesByIdFiltering() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        Long id = cashBookBalance.getId();

        defaultCashBookBalanceShouldBeFound("id.equals=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultCashBookBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultCashBookBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance equals to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance equals to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the cashBookBalanceList where balance equals to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is not null
        defaultCashBookBalanceShouldBeFound("balance.specified=true");

        // Get all the cashBookBalanceList where balance is null
        defaultCashBookBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is less than DEFAULT_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is less than UPDATED_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is greater than DEFAULT_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is greater than SMALLER_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            cashBookBalanceRepository.saveAndFlush(cashBookBalance);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        cashBookBalance.setMerchant(merchant);
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);
        Long merchantId = merchant.getId();

        // Get all the cashBookBalanceList where merchant equals to merchantId
        defaultCashBookBalanceShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the cashBookBalanceList where merchant equals to (merchantId + 1)
        defaultCashBookBalanceShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllCashBookBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            cashBookBalanceRepository.saveAndFlush(cashBookBalance);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        cashBookBalance.setTransactionType(transactionType);
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashBookBalanceList where transactionType equals to transactionTypeId
        defaultCashBookBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashBookBalanceList where transactionType equals to (transactionTypeId + 1)
        defaultCashBookBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookBalanceShouldBeFound(String filter) throws Exception {
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));

        // Check, that the count call also returns 1
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookBalanceShouldNotBeFound(String filter) throws Exception {
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCashBookBalance() throws Exception {
        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance
        CashBookBalance updatedCashBookBalance = cashBookBalanceRepository.findById(cashBookBalance.getId()).get();
        // Disconnect from session so that the updates on updatedCashBookBalance are not directly saved in db
        em.detach(updatedCashBookBalance);
        updatedCashBookBalance.balance(UPDATED_BALANCE);

        restCashBookBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashBookBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCashBookBalance))
            )
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashBookBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashBookBalanceWithPatch() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance using partial update
        CashBookBalance partialUpdatedCashBookBalance = new CashBookBalance();
        partialUpdatedCashBookBalance.setId(cashBookBalance.getId());

        restCashBookBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBookBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashBookBalance))
            )
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateCashBookBalanceWithPatch() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance using partial update
        CashBookBalance partialUpdatedCashBookBalance = new CashBookBalance();
        partialUpdatedCashBookBalance.setId(cashBookBalance.getId());

        partialUpdatedCashBookBalance.balance(UPDATED_BALANCE);

        restCashBookBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBookBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashBookBalance))
            )
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashBookBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();
        cashBookBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashBookBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeDelete = cashBookBalanceRepository.findAll().size();

        // Delete the cashBookBalance
        restCashBookBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashBookBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
