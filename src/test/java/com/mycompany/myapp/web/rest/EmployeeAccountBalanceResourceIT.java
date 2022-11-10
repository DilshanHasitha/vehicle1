package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.EmployeeAccountBalance;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.EmployeeAccountBalanceRepository;
import com.mycompany.myapp.service.criteria.EmployeeAccountBalanceCriteria;
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
 * Integration tests for the {@link EmployeeAccountBalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeAccountBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/employee-account-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeAccountBalanceMockMvc;

    private EmployeeAccountBalance employeeAccountBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccountBalance createEntity(EntityManager em) {
        EmployeeAccountBalance employeeAccountBalance = new EmployeeAccountBalance().balance(DEFAULT_BALANCE);
        return employeeAccountBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccountBalance createUpdatedEntity(EntityManager em) {
        EmployeeAccountBalance employeeAccountBalance = new EmployeeAccountBalance().balance(UPDATED_BALANCE);
        return employeeAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        employeeAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountBalanceRepository.findAll().size();
        // Create the EmployeeAccountBalance
        restEmployeeAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void createEmployeeAccountBalanceWithExistingId() throws Exception {
        // Create the EmployeeAccountBalance with an existing ID
        employeeAccountBalance.setId(1L);

        int databaseSizeBeforeCreate = employeeAccountBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountBalanceRepository.findAll().size();
        // set the field null
        employeeAccountBalance.setBalance(null);

        // Create the EmployeeAccountBalance, which fails.

        restEmployeeAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalances() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));
    }

    @Test
    @Transactional
    void getEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    void getEmployeeAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        Long id = employeeAccountBalance.getId();

        defaultEmployeeAccountBalanceShouldBeFound("id.equals=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the employeeAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is not null
        defaultEmployeeAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the employeeAccountBalanceList where balance is null
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        employeeAccountBalance.setEmployee(employee);
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long employeeId = employee.getId();

        // Get all the employeeAccountBalanceList where employee equals to employeeId
        defaultEmployeeAccountBalanceShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employeeAccountBalanceList where employee equals to (employeeId + 1)
        defaultEmployeeAccountBalanceShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        employeeAccountBalance.setMerchant(merchant);
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long merchantId = merchant.getId();

        // Get all the employeeAccountBalanceList where merchant equals to merchantId
        defaultEmployeeAccountBalanceShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the employeeAccountBalanceList where merchant equals to (merchantId + 1)
        defaultEmployeeAccountBalanceShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeeAccountBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
            transactionType = TransactionTypeResourceIT.createEntity(em);
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        employeeAccountBalance.setTransactionType(transactionType);
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the employeeAccountBalanceList where transactionType equals to transactionTypeId
        defaultEmployeeAccountBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the employeeAccountBalanceList where transactionType equals to (transactionTypeId + 1)
        defaultEmployeeAccountBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeAccountBalanceShouldBeFound(String filter) throws Exception {
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));

        // Check, that the count call also returns 1
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeAccountBalance() throws Exception {
        // Get the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();

        // Update the employeeAccountBalance
        EmployeeAccountBalance updatedEmployeeAccountBalance = employeeAccountBalanceRepository
            .findById(employeeAccountBalance.getId())
            .get();
        // Disconnect from session so that the updates on updatedEmployeeAccountBalance are not directly saved in db
        em.detach(updatedEmployeeAccountBalance);
        updatedEmployeeAccountBalance.balance(UPDATED_BALANCE);

        restEmployeeAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();

        // Update the employeeAccountBalance using partial update
        EmployeeAccountBalance partialUpdatedEmployeeAccountBalance = new EmployeeAccountBalance();
        partialUpdatedEmployeeAccountBalance.setId(employeeAccountBalance.getId());

        restEmployeeAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();

        // Update the employeeAccountBalance using partial update
        EmployeeAccountBalance partialUpdatedEmployeeAccountBalance = new EmployeeAccountBalance();
        partialUpdatedEmployeeAccountBalance.setId(employeeAccountBalance.getId());

        partialUpdatedEmployeeAccountBalance.balance(UPDATED_BALANCE);

        restEmployeeAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();
        employeeAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        int databaseSizeBeforeDelete = employeeAccountBalanceRepository.findAll().size();

        // Delete the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeAccountBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
