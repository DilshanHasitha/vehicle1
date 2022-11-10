package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ExpenseAccountBalance;
import com.mycompany.myapp.repository.ExpenseAccountBalanceRepository;
import com.mycompany.myapp.service.ExpenseAccountBalanceQueryService;
import com.mycompany.myapp.service.ExpenseAccountBalanceService;
import com.mycompany.myapp.service.criteria.ExpenseAccountBalanceCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ExpenseAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountBalanceResource.class);

    private static final String ENTITY_NAME = "expenseAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseAccountBalanceService expenseAccountBalanceService;

    private final ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    private final ExpenseAccountBalanceQueryService expenseAccountBalanceQueryService;

    public ExpenseAccountBalanceResource(
        ExpenseAccountBalanceService expenseAccountBalanceService,
        ExpenseAccountBalanceRepository expenseAccountBalanceRepository,
        ExpenseAccountBalanceQueryService expenseAccountBalanceQueryService
    ) {
        this.expenseAccountBalanceService = expenseAccountBalanceService;
        this.expenseAccountBalanceRepository = expenseAccountBalanceRepository;
        this.expenseAccountBalanceQueryService = expenseAccountBalanceQueryService;
    }

    /**
     * {@code POST  /expense-account-balances} : Create a new expenseAccountBalance.
     *
     * @param expenseAccountBalance the expenseAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseAccountBalance, or with status {@code 400 (Bad Request)} if the expenseAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-account-balances")
    public ResponseEntity<ExpenseAccountBalance> createExpenseAccountBalance(
        @Valid @RequestBody ExpenseAccountBalance expenseAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to save ExpenseAccountBalance : {}", expenseAccountBalance);
        if (expenseAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new expenseAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseAccountBalance result = expenseAccountBalanceService.save(expenseAccountBalance);
        return ResponseEntity
            .created(new URI("/api/expense-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-account-balances/:id} : Updates an existing expenseAccountBalance.
     *
     * @param id the id of the expenseAccountBalance to save.
     * @param expenseAccountBalance the expenseAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccountBalance,
     * or with status {@code 400 (Bad Request)} if the expenseAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-account-balances/{id}")
    public ResponseEntity<ExpenseAccountBalance> updateExpenseAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpenseAccountBalance expenseAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to update ExpenseAccountBalance : {}, {}", id, expenseAccountBalance);
        if (expenseAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpenseAccountBalance result = expenseAccountBalanceService.update(expenseAccountBalance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expense-account-balances/:id} : Partial updates given fields of an existing expenseAccountBalance, field will ignore if it is null
     *
     * @param id the id of the expenseAccountBalance to save.
     * @param expenseAccountBalance the expenseAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccountBalance,
     * or with status {@code 400 (Bad Request)} if the expenseAccountBalance is not valid,
     * or with status {@code 404 (Not Found)} if the expenseAccountBalance is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expense-account-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseAccountBalance> partialUpdateExpenseAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpenseAccountBalance expenseAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpenseAccountBalance partially : {}, {}", id, expenseAccountBalance);
        if (expenseAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseAccountBalance> result = expenseAccountBalanceService.partialUpdate(expenseAccountBalance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccountBalance.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-account-balances} : get all the expenseAccountBalances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseAccountBalances in body.
     */
    @GetMapping("/expense-account-balances")
    public ResponseEntity<List<ExpenseAccountBalance>> getAllExpenseAccountBalances(
        ExpenseAccountBalanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ExpenseAccountBalances by criteria: {}", criteria);
        Page<ExpenseAccountBalance> page = expenseAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-account-balances/count} : count all the expenseAccountBalances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expense-account-balances/count")
    public ResponseEntity<Long> countExpenseAccountBalances(ExpenseAccountBalanceCriteria criteria) {
        log.debug("REST request to count ExpenseAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-account-balances/:id} : get the "id" expenseAccountBalance.
     *
     * @param id the id of the expenseAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-account-balances/{id}")
    public ResponseEntity<ExpenseAccountBalance> getExpenseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get ExpenseAccountBalance : {}", id);
        Optional<ExpenseAccountBalance> expenseAccountBalance = expenseAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseAccountBalance);
    }

    /**
     * {@code DELETE  /expense-account-balances/:id} : delete the "id" expenseAccountBalance.
     *
     * @param id the id of the expenseAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-account-balances/{id}")
    public ResponseEntity<Void> deleteExpenseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseAccountBalance : {}", id);
        expenseAccountBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
