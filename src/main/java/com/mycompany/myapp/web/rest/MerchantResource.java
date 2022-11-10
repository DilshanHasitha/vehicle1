package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.service.MerchantQueryService;
import com.mycompany.myapp.service.MerchantService;
import com.mycompany.myapp.service.criteria.MerchantCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Merchant}.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);

    private static final String ENTITY_NAME = "merchant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MerchantService merchantService;

    private final MerchantRepository merchantRepository;

    private final MerchantQueryService merchantQueryService;

    public MerchantResource(
        MerchantService merchantService,
        MerchantRepository merchantRepository,
        MerchantQueryService merchantQueryService
    ) {
        this.merchantService = merchantService;
        this.merchantRepository = merchantRepository;
        this.merchantQueryService = merchantQueryService;
    }

    /**
     * {@code POST  /merchants} : Create a new merchant.
     *
     * @param merchant the merchant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchant, or with status {@code 400 (Bad Request)} if the merchant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/merchants")
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody Merchant merchant) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchant);
        if (merchant.getId() != null) {
            throw new BadRequestAlertException("A new merchant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Merchant result = merchantService.save(merchant);
        return ResponseEntity
            .created(new URI("/api/merchants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /merchants/:id} : Updates an existing merchant.
     *
     * @param id the id of the merchant to save.
     * @param merchant the merchant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchant,
     * or with status {@code 400 (Bad Request)} if the merchant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/merchants/{id}")
    public ResponseEntity<Merchant> updateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Merchant merchant
    ) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}, {}", id, merchant);
        if (merchant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Merchant result = merchantService.update(merchant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, merchant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /merchants/:id} : Partial updates given fields of an existing merchant, field will ignore if it is null
     *
     * @param id the id of the merchant to save.
     * @param merchant the merchant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchant,
     * or with status {@code 400 (Bad Request)} if the merchant is not valid,
     * or with status {@code 404 (Not Found)} if the merchant is not found,
     * or with status {@code 500 (Internal Server Error)} if the merchant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/merchants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Merchant> partialUpdateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Merchant merchant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Merchant partially : {}, {}", id, merchant);
        if (merchant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Merchant> result = merchantService.partialUpdate(merchant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, merchant.getId().toString())
        );
    }

    /**
     * {@code GET  /merchants} : get all the merchants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchants in body.
     */
    @GetMapping("/merchants")
    public ResponseEntity<List<Merchant>> getAllMerchants(
        MerchantCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Merchants by criteria: {}", criteria);
        Page<Merchant> page = merchantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /merchants/count} : count all the merchants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/merchants/count")
    public ResponseEntity<Long> countMerchants(MerchantCriteria criteria) {
        log.debug("REST request to count Merchants by criteria: {}", criteria);
        return ResponseEntity.ok().body(merchantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /merchants/:id} : get the "id" merchant.
     *
     * @param id the id of the merchant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/merchants/{id}")
    public ResponseEntity<Merchant> getMerchant(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        Optional<Merchant> merchant = merchantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(merchant);
    }

    /**
     * {@code DELETE  /merchants/:id} : delete the "id" merchant.
     *
     * @param id the id of the merchant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/merchants/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
