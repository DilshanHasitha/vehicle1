package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Banners;
import com.mycompany.myapp.repository.BannersRepository;
import com.mycompany.myapp.service.BannersQueryService;
import com.mycompany.myapp.service.BannersService;
import com.mycompany.myapp.service.criteria.BannersCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Banners}.
 */
@RestController
@RequestMapping("/api")
public class BannersResource {

    private final Logger log = LoggerFactory.getLogger(BannersResource.class);

    private static final String ENTITY_NAME = "banners";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BannersService bannersService;

    private final BannersRepository bannersRepository;

    private final BannersQueryService bannersQueryService;

    public BannersResource(BannersService bannersService, BannersRepository bannersRepository, BannersQueryService bannersQueryService) {
        this.bannersService = bannersService;
        this.bannersRepository = bannersRepository;
        this.bannersQueryService = bannersQueryService;
    }

    /**
     * {@code POST  /banners} : Create a new banners.
     *
     * @param banners the banners to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new banners, or with status {@code 400 (Bad Request)} if the banners has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banners")
    public ResponseEntity<Banners> createBanners(@RequestBody Banners banners) throws URISyntaxException {
        log.debug("REST request to save Banners : {}", banners);
        if (banners.getId() != null) {
            throw new BadRequestAlertException("A new banners cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Banners result = bannersService.save(banners);
        return ResponseEntity
            .created(new URI("/api/banners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banners/:id} : Updates an existing banners.
     *
     * @param id the id of the banners to save.
     * @param banners the banners to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banners,
     * or with status {@code 400 (Bad Request)} if the banners is not valid,
     * or with status {@code 500 (Internal Server Error)} if the banners couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banners/{id}")
    public ResponseEntity<Banners> updateBanners(@PathVariable(value = "id", required = false) final Long id, @RequestBody Banners banners)
        throws URISyntaxException {
        log.debug("REST request to update Banners : {}, {}", id, banners);
        if (banners.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banners.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Banners result = bannersService.update(banners);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, banners.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /banners/:id} : Partial updates given fields of an existing banners, field will ignore if it is null
     *
     * @param id the id of the banners to save.
     * @param banners the banners to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banners,
     * or with status {@code 400 (Bad Request)} if the banners is not valid,
     * or with status {@code 404 (Not Found)} if the banners is not found,
     * or with status {@code 500 (Internal Server Error)} if the banners couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/banners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Banners> partialUpdateBanners(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Banners banners
    ) throws URISyntaxException {
        log.debug("REST request to partial update Banners partially : {}, {}", id, banners);
        if (banners.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banners.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Banners> result = bannersService.partialUpdate(banners);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, banners.getId().toString())
        );
    }

    /**
     * {@code GET  /banners} : get all the banners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banners in body.
     */
    @GetMapping("/banners")
    public ResponseEntity<List<Banners>> getAllBanners(
        BannersCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Banners by criteria: {}", criteria);
        Page<Banners> page = bannersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banners/count} : count all the banners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/banners/count")
    public ResponseEntity<Long> countBanners(BannersCriteria criteria) {
        log.debug("REST request to count Banners by criteria: {}", criteria);
        return ResponseEntity.ok().body(bannersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /banners/:id} : get the "id" banners.
     *
     * @param id the id of the banners to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the banners, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banners/{id}")
    public ResponseEntity<Banners> getBanners(@PathVariable Long id) {
        log.debug("REST request to get Banners : {}", id);
        Optional<Banners> banners = bannersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(banners);
    }

    /**
     * {@code DELETE  /banners/:id} : delete the "id" banners.
     *
     * @param id the id of the banners to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banners/{id}")
    public ResponseEntity<Void> deleteBanners(@PathVariable Long id) {
        log.debug("REST request to delete Banners : {}", id);
        bannersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
