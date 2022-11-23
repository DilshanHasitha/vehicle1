package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.domain.ExpenseAccount;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.repository.ExpenseAccountRepository;
import com.mycompany.myapp.repository.ExpenseRepository;
import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.ExpenseAccountQueryService;
import com.mycompany.myapp.service.ExpenseAccountService;
import com.mycompany.myapp.service.criteria.ExpenseAccountCriteria;
import com.mycompany.myapp.service.dto.AddExpenseDTO;
import com.mycompany.myapp.service.dto.RequestTransDTO;
import com.mycompany.myapp.service.dto.TransactionDTO;
import com.mycompany.myapp.service.dto.TrnByExpenseCodeDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExpenseAccount}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseAccountResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountResource.class);

    private static final String ENTITY_NAME = "expenseAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseAccountService expenseAccountService;

    private final ExpenseAccountRepository expenseAccountRepository;

    private final ExpenseAccountQueryService expenseAccountQueryService;

    private final ExpenseRepository expenseRepository;
    private final MerchantRepository merchantRepository;

    public ExpenseAccountResource(
        ExpenseAccountService expenseAccountService,
        ExpenseAccountRepository expenseAccountRepository,
        ExpenseAccountQueryService expenseAccountQueryService,
        ExpenseRepository expenseRepository,
        MerchantRepository merchantRepository
    ) {
        this.expenseAccountService = expenseAccountService;
        this.expenseAccountRepository = expenseAccountRepository;
        this.expenseAccountQueryService = expenseAccountQueryService;
        this.expenseRepository = expenseRepository;
        this.merchantRepository = merchantRepository;
    }

    /**
     * {@code POST  /expense-accounts} : Create a new expenseAccount.
     *
     * @param expenseAccount the expenseAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseAccount, or with status {@code 400 (Bad Request)} if the expenseAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-accounts")
    public ResponseEntity<ExpenseAccount> createExpenseAccount(@Valid @RequestBody ExpenseAccount expenseAccount)
        throws URISyntaxException {
        log.debug("REST request to save ExpenseAccount : {}", expenseAccount);
        if (expenseAccount.getId() != null) {
            throw new BadRequestAlertException("A new expenseAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseAccount result = expenseAccountService.save(expenseAccount);
        return ResponseEntity
            .created(new URI("/api/expense-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-accounts/:id} : Updates an existing expenseAccount.
     *
     * @param id the id of the expenseAccount to save.
     * @param expenseAccount the expenseAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccount,
     * or with status {@code 400 (Bad Request)} if the expenseAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-accounts/{id}")
    public ResponseEntity<ExpenseAccount> updateExpenseAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpenseAccount expenseAccount
    ) throws URISyntaxException {
        log.debug("REST request to update ExpenseAccount : {}, {}", id, expenseAccount);
        if (expenseAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpenseAccount result = expenseAccountService.update(expenseAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expense-accounts/:id} : Partial updates given fields of an existing expenseAccount, field will ignore if it is null
     *
     * @param id the id of the expenseAccount to save.
     * @param expenseAccount the expenseAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccount,
     * or with status {@code 400 (Bad Request)} if the expenseAccount is not valid,
     * or with status {@code 404 (Not Found)} if the expenseAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expense-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseAccount> partialUpdateExpenseAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpenseAccount expenseAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpenseAccount partially : {}, {}", id, expenseAccount);
        if (expenseAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseAccount> result = expenseAccountService.partialUpdate(expenseAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-accounts} : get all the expenseAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseAccounts in body.
     */
    @GetMapping("/expense-accounts")
    public ResponseEntity<List<ExpenseAccount>> getAllExpenseAccounts(
        ExpenseAccountCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ExpenseAccounts by criteria: {}", criteria);
        Page<ExpenseAccount> page = expenseAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-accounts/count} : count all the expenseAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expense-accounts/count")
    public ResponseEntity<Long> countExpenseAccounts(ExpenseAccountCriteria criteria) {
        log.debug("REST request to count ExpenseAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-accounts/:id} : get the "id" expenseAccount.
     *
     * @param id the id of the expenseAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-accounts/{id}")
    public ResponseEntity<ExpenseAccount> getExpenseAccount(@PathVariable Long id) {
        log.debug("REST request to get ExpenseAccount : {}", id);
        Optional<ExpenseAccount> expenseAccount = expenseAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseAccount);
    }

    /**
     * {@code DELETE  /expense-accounts/:id} : delete the "id" expenseAccount.
     *
     * @param id the id of the expenseAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-accounts/{id}")
    public ResponseEntity<Void> deleteExpenseAccount(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseAccount : {}", id);
        expenseAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/Transaction")
    public ResponseEntity<List<TransactionDTO>> transfer(@Valid @RequestBody RequestTransDTO requestTransDTO) throws URISyntaxException {
        log.debug("REST request for a Mobile Bill Settlement : {}", requestTransDTO);
        List<TransactionDTO> transactions = expenseAccountService.getTransaction(requestTransDTO);
        return ResponseEntity.ok().body(transactions);
    }

    @PostMapping("/addExpenses")
    public ResponseEntity<ExpenseAccount> addExpenses(@Valid @RequestBody AddExpenseDTO expenseAccount) throws URISyntaxException {
        ExpenseAccount expenseAccount1 = new ExpenseAccount();
        BigDecimal cr = BigDecimal.ZERO;
        BigDecimal dr = BigDecimal.ZERO;
        if (expenseAccount.getCr() != null) {
            cr = expenseAccount.getCr();
        }
        if (expenseAccount.getDr() != null) {
            dr = expenseAccount.getDr();
        }

        Expense expense = expenseRepository.findOneByExpenseCode(expenseAccount.getExpenseCode());
        Merchant merchant = merchantRepository.findOneByCode(expenseAccount.getMerchantCode());

        expenseAccount1.setTransactionAmountCR(cr);
        expenseAccount1.setTransactionAmountDR(dr);
        expenseAccount1.setTransactionBalance(expenseAccount.getAmount());
        expenseAccount1.setTransactionDescription(
            expenseAccount.getDescription() +
            "" +
            "-" +
            expenseAccount.getExpenseCode() +
            "" +
            "-" +
            SecurityUtils.getCurrentUserLogin().get()
        );
        expenseAccount1.setExpense(expense);
        expenseAccount1.setMerchant(merchant);
        expenseAccount1.setTransactionDate(LocalDate.now());

        ExpenseAccount result = expenseAccountService.save(expenseAccount1);
        return ResponseEntity
            .created(new URI("/api/expense-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/getTrnByExpenseCode")
    public ResponseEntity<List<ExpenseAccount>> getTrnByExpenseCode(@Valid @RequestBody TrnByExpenseCodeDTO trnByExpenseCodeDTO)
        throws URISyntaxException {
        log.debug("REST request for a Mobile Bill Settlement : {}", trnByExpenseCodeDTO);
        List<ExpenseAccount> transactions = new ArrayList<>();
        if (
            expenseAccountRepository.findAllByExpense_ExpenseCode(trnByExpenseCodeDTO.getExpenseCode()).isPresent() &&
            !expenseAccountRepository.findAllByExpense_ExpenseCode(trnByExpenseCodeDTO.getExpenseCode()).isEmpty()
        ) {
            transactions = expenseAccountRepository.findAllByExpense_ExpenseCode(trnByExpenseCodeDTO.getExpenseCode()).get();
            return ResponseEntity.ok().body(transactions);
        } else {
            return ResponseEntity.ok().body(transactions);
        }
    }
}
