package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmployeeAccountBalance;
import com.mycompany.myapp.repository.EmployeeAccountBalanceRepository;
import com.mycompany.myapp.service.EmployeeAccountBalanceQueryService;
import com.mycompany.myapp.service.EmployeeAccountBalanceService;
import com.mycompany.myapp.service.criteria.EmployeeAccountBalanceCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EmployeeAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountBalanceResource.class);

    private static final String ENTITY_NAME = "employeeAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeAccountBalanceService employeeAccountBalanceService;

    private final EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    private final EmployeeAccountBalanceQueryService employeeAccountBalanceQueryService;

    public EmployeeAccountBalanceResource(
        EmployeeAccountBalanceService employeeAccountBalanceService,
        EmployeeAccountBalanceRepository employeeAccountBalanceRepository,
        EmployeeAccountBalanceQueryService employeeAccountBalanceQueryService
    ) {
        this.employeeAccountBalanceService = employeeAccountBalanceService;
        this.employeeAccountBalanceRepository = employeeAccountBalanceRepository;
        this.employeeAccountBalanceQueryService = employeeAccountBalanceQueryService;
    }

    /**
     * {@code POST  /employee-account-balances} : Create a new employeeAccountBalance.
     *
     * @param employeeAccountBalance the employeeAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeAccountBalance, or with status {@code 400 (Bad Request)} if the employeeAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-account-balances")
    public ResponseEntity<EmployeeAccountBalance> createEmployeeAccountBalance(
        @Valid @RequestBody EmployeeAccountBalance employeeAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to save EmployeeAccountBalance : {}", employeeAccountBalance);
        if (employeeAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new employeeAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeAccountBalance result = employeeAccountBalanceService.save(employeeAccountBalance);
        return ResponseEntity
            .created(new URI("/api/employee-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-account-balances/:id} : Updates an existing employeeAccountBalance.
     *
     * @param id the id of the employeeAccountBalance to save.
     * @param employeeAccountBalance the employeeAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAccountBalance,
     * or with status {@code 400 (Bad Request)} if the employeeAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-account-balances/{id}")
    public ResponseEntity<EmployeeAccountBalance> updateEmployeeAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeAccountBalance employeeAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeAccountBalance : {}, {}", id, employeeAccountBalance);
        if (employeeAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeeAccountBalance result = employeeAccountBalanceService.update(employeeAccountBalance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employee-account-balances/:id} : Partial updates given fields of an existing employeeAccountBalance, field will ignore if it is null
     *
     * @param id the id of the employeeAccountBalance to save.
     * @param employeeAccountBalance the employeeAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAccountBalance,
     * or with status {@code 400 (Bad Request)} if the employeeAccountBalance is not valid,
     * or with status {@code 404 (Not Found)} if the employeeAccountBalance is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employee-account-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeAccountBalance> partialUpdateEmployeeAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeAccountBalance employeeAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeAccountBalance partially : {}, {}", id, employeeAccountBalance);
        if (employeeAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeAccountBalance> result = employeeAccountBalanceService.partialUpdate(employeeAccountBalance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAccountBalance.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-account-balances} : get all the employeeAccountBalances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeAccountBalances in body.
     */
    @GetMapping("/employee-account-balances")
    public ResponseEntity<List<EmployeeAccountBalance>> getAllEmployeeAccountBalances(
        EmployeeAccountBalanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EmployeeAccountBalances by criteria: {}", criteria);
        Page<EmployeeAccountBalance> page = employeeAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-account-balances/count} : count all the employeeAccountBalances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employee-account-balances/count")
    public ResponseEntity<Long> countEmployeeAccountBalances(EmployeeAccountBalanceCriteria criteria) {
        log.debug("REST request to count EmployeeAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-account-balances/:id} : get the "id" employeeAccountBalance.
     *
     * @param id the id of the employeeAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-account-balances/{id}")
    public ResponseEntity<EmployeeAccountBalance> getEmployeeAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAccountBalance : {}", id);
        Optional<EmployeeAccountBalance> employeeAccountBalance = employeeAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAccountBalance);
    }

    /**
     * {@code DELETE  /employee-account-balances/:id} : delete the "id" employeeAccountBalance.
     *
     * @param id the id of the employeeAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-account-balances/{id}")
    public ResponseEntity<Void> deleteEmployeeAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAccountBalance : {}", id);
        employeeAccountBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
