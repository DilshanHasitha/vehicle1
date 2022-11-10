package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmployeeAccount;
import com.mycompany.myapp.repository.EmployeeAccountRepository;
import com.mycompany.myapp.service.EmployeeAccountQueryService;
import com.mycompany.myapp.service.EmployeeAccountService;
import com.mycompany.myapp.service.criteria.EmployeeAccountCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EmployeeAccount}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAccountResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountResource.class);

    private static final String ENTITY_NAME = "employeeAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeAccountService employeeAccountService;

    private final EmployeeAccountRepository employeeAccountRepository;

    private final EmployeeAccountQueryService employeeAccountQueryService;

    public EmployeeAccountResource(
        EmployeeAccountService employeeAccountService,
        EmployeeAccountRepository employeeAccountRepository,
        EmployeeAccountQueryService employeeAccountQueryService
    ) {
        this.employeeAccountService = employeeAccountService;
        this.employeeAccountRepository = employeeAccountRepository;
        this.employeeAccountQueryService = employeeAccountQueryService;
    }

    /**
     * {@code POST  /employee-accounts} : Create a new employeeAccount.
     *
     * @param employeeAccount the employeeAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeAccount, or with status {@code 400 (Bad Request)} if the employeeAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-accounts")
    public ResponseEntity<EmployeeAccount> createEmployeeAccount(@Valid @RequestBody EmployeeAccount employeeAccount)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeAccount : {}", employeeAccount);
        if (employeeAccount.getId() != null) {
            throw new BadRequestAlertException("A new employeeAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeAccount result = employeeAccountService.save(employeeAccount);
        return ResponseEntity
            .created(new URI("/api/employee-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-accounts/:id} : Updates an existing employeeAccount.
     *
     * @param id the id of the employeeAccount to save.
     * @param employeeAccount the employeeAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAccount,
     * or with status {@code 400 (Bad Request)} if the employeeAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-accounts/{id}")
    public ResponseEntity<EmployeeAccount> updateEmployeeAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeAccount employeeAccount
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeAccount : {}, {}", id, employeeAccount);
        if (employeeAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeeAccount result = employeeAccountService.update(employeeAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employee-accounts/:id} : Partial updates given fields of an existing employeeAccount, field will ignore if it is null
     *
     * @param id the id of the employeeAccount to save.
     * @param employeeAccount the employeeAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAccount,
     * or with status {@code 400 (Bad Request)} if the employeeAccount is not valid,
     * or with status {@code 404 (Not Found)} if the employeeAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employee-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeAccount> partialUpdateEmployeeAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeAccount employeeAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeAccount partially : {}, {}", id, employeeAccount);
        if (employeeAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeAccount> result = employeeAccountService.partialUpdate(employeeAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-accounts} : get all the employeeAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeAccounts in body.
     */
    @GetMapping("/employee-accounts")
    public ResponseEntity<List<EmployeeAccount>> getAllEmployeeAccounts(
        EmployeeAccountCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EmployeeAccounts by criteria: {}", criteria);
        Page<EmployeeAccount> page = employeeAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-accounts/count} : count all the employeeAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employee-accounts/count")
    public ResponseEntity<Long> countEmployeeAccounts(EmployeeAccountCriteria criteria) {
        log.debug("REST request to count EmployeeAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-accounts/:id} : get the "id" employeeAccount.
     *
     * @param id the id of the employeeAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-accounts/{id}")
    public ResponseEntity<EmployeeAccount> getEmployeeAccount(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAccount : {}", id);
        Optional<EmployeeAccount> employeeAccount = employeeAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAccount);
    }

    /**
     * {@code DELETE  /employee-accounts/:id} : delete the "id" employeeAccount.
     *
     * @param id the id of the employeeAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-accounts/{id}")
    public ResponseEntity<Void> deleteEmployeeAccount(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAccount : {}", id);
        employeeAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
