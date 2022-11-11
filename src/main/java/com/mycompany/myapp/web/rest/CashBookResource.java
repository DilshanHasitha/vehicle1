package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.repository.CashBookRepository;
import com.mycompany.myapp.service.CashBookQueryService;
import com.mycompany.myapp.service.CashBookService;
import com.mycompany.myapp.service.criteria.CashBookCriteria;
import com.mycompany.myapp.service.dto.RequestTransDTO;
import com.mycompany.myapp.service.dto.TransactionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CashBook}.
 */
@RestController
@RequestMapping("/api")
public class CashBookResource {

    private final Logger log = LoggerFactory.getLogger(CashBookResource.class);

    private static final String ENTITY_NAME = "cashBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashBookService cashBookService;

    private final CashBookRepository cashBookRepository;

    private final CashBookQueryService cashBookQueryService;

    public CashBookResource(
        CashBookService cashBookService,
        CashBookRepository cashBookRepository,
        CashBookQueryService cashBookQueryService
    ) {
        this.cashBookService = cashBookService;
        this.cashBookRepository = cashBookRepository;
        this.cashBookQueryService = cashBookQueryService;
    }

    /**
     * {@code POST  /cash-books} : Create a new cashBook.
     *
     * @param cashBook the cashBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashBook, or with status {@code 400 (Bad Request)} if the cashBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-books")
    public ResponseEntity<CashBook> createCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        log.debug("REST request to save CashBook : {}", cashBook);
        if (cashBook.getId() != null) {
            throw new BadRequestAlertException("A new cashBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashBook result = cashBookService.save(cashBook);
        return ResponseEntity
            .created(new URI("/api/cash-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-books/:id} : Updates an existing cashBook.
     *
     * @param id the id of the cashBook to save.
     * @param cashBook the cashBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBook,
     * or with status {@code 400 (Bad Request)} if the cashBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-books/{id}")
    public ResponseEntity<CashBook> updateCashBook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CashBook cashBook
    ) throws URISyntaxException {
        log.debug("REST request to update CashBook : {}, {}", id, cashBook);
        if (cashBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CashBook result = cashBookService.update(cashBook);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashBook.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cash-books/:id} : Partial updates given fields of an existing cashBook, field will ignore if it is null
     *
     * @param id the id of the cashBook to save.
     * @param cashBook the cashBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBook,
     * or with status {@code 400 (Bad Request)} if the cashBook is not valid,
     * or with status {@code 404 (Not Found)} if the cashBook is not found,
     * or with status {@code 500 (Internal Server Error)} if the cashBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cash-books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CashBook> partialUpdateCashBook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CashBook cashBook
    ) throws URISyntaxException {
        log.debug("REST request to partial update CashBook partially : {}, {}", id, cashBook);
        if (cashBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CashBook> result = cashBookService.partialUpdate(cashBook);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashBook.getId().toString())
        );
    }

    /**
     * {@code GET  /cash-books} : get all the cashBooks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashBooks in body.
     */
    @GetMapping("/cash-books")
    public ResponseEntity<List<CashBook>> getAllCashBooks(
        CashBookCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CashBooks by criteria: {}", criteria);
        Page<CashBook> page = cashBookQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cash-books/count} : count all the cashBooks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cash-books/count")
    public ResponseEntity<Long> countCashBooks(CashBookCriteria criteria) {
        log.debug("REST request to count CashBooks by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashBookQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-books/:id} : get the "id" cashBook.
     *
     * @param id the id of the cashBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-books/{id}")
    public ResponseEntity<CashBook> getCashBook(@PathVariable Long id) {
        log.debug("REST request to get CashBook : {}", id);
        Optional<CashBook> cashBook = cashBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashBook);
    }

    /**
     * {@code DELETE  /cash-books/:id} : delete the "id" cashBook.
     *
     * @param id the id of the cashBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-books/{id}")
    public ResponseEntity<Void> deleteCashBook(@PathVariable Long id) {
        log.debug("REST request to delete CashBook : {}", id);
        cashBookService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/getCashBookByDate")
    public ResponseEntity<TransactionDTO> findAllByDate(@Valid @RequestBody RequestTransDTO requestTransDTO) throws URISyntaxException {
        log.debug("REST request for a Mobile Bill Settlement : {}", requestTransDTO);
        List<CashBook> cashBooks = cashBookService.findAllByDate(requestTransDTO).get();

        BigDecimal cr = BigDecimal.ZERO;
        BigDecimal dr = BigDecimal.ZERO;

        TransactionDTO transactions = new TransactionDTO();
        for (CashBook cashBook : cashBooks) {
            cr = cr.add(cashBook.getTransactionAmountCR());
            dr = dr.add(cashBook.getTransactionAmountDR());
        }
        transactions.setDr(dr);
        transactions.setCr(cr);
        transactions.setDate(requestTransDTO.getDate());
        transactions.setVehicleNo("all");

        return ResponseEntity.ok().body(transactions);
    }
}
