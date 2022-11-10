package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.repository.ImagesRepository;
import com.mycompany.myapp.service.ImagesQueryService;
import com.mycompany.myapp.service.ImagesService;
import com.mycompany.myapp.service.criteria.ImagesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Images}.
 */
@RestController
@RequestMapping("/api")
public class ImagesResource {

    private final Logger log = LoggerFactory.getLogger(ImagesResource.class);

    private static final String ENTITY_NAME = "images";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagesService imagesService;

    private final ImagesRepository imagesRepository;

    private final ImagesQueryService imagesQueryService;

    public ImagesResource(ImagesService imagesService, ImagesRepository imagesRepository, ImagesQueryService imagesQueryService) {
        this.imagesService = imagesService;
        this.imagesRepository = imagesRepository;
        this.imagesQueryService = imagesQueryService;
    }

    /**
     * {@code POST  /images} : Create a new images.
     *
     * @param images the images to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new images, or with status {@code 400 (Bad Request)} if the images has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/images")
    public ResponseEntity<Images> createImages(@RequestBody Images images) throws URISyntaxException {
        log.debug("REST request to save Images : {}", images);
        if (images.getId() != null) {
            throw new BadRequestAlertException("A new images cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Images result = imagesService.save(images);
        return ResponseEntity
            .created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /images/:id} : Updates an existing images.
     *
     * @param id the id of the images to save.
     * @param images the images to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated images,
     * or with status {@code 400 (Bad Request)} if the images is not valid,
     * or with status {@code 500 (Internal Server Error)} if the images couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/images/{id}")
    public ResponseEntity<Images> updateImages(@PathVariable(value = "id", required = false) final Long id, @RequestBody Images images)
        throws URISyntaxException {
        log.debug("REST request to update Images : {}, {}", id, images);
        if (images.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, images.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Images result = imagesService.update(images);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, images.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /images/:id} : Partial updates given fields of an existing images, field will ignore if it is null
     *
     * @param id the id of the images to save.
     * @param images the images to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated images,
     * or with status {@code 400 (Bad Request)} if the images is not valid,
     * or with status {@code 404 (Not Found)} if the images is not found,
     * or with status {@code 500 (Internal Server Error)} if the images couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Images> partialUpdateImages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Images images
    ) throws URISyntaxException {
        log.debug("REST request to partial update Images partially : {}, {}", id, images);
        if (images.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, images.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Images> result = imagesService.partialUpdate(images);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, images.getId().toString())
        );
    }

    /**
     * {@code GET  /images} : get all the images.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
    @GetMapping("/images")
    public ResponseEntity<List<Images>> getAllImages(
        ImagesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Images by criteria: {}", criteria);
        Page<Images> page = imagesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /images/count} : count all the images.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/images/count")
    public ResponseEntity<Long> countImages(ImagesCriteria criteria) {
        log.debug("REST request to count Images by criteria: {}", criteria);
        return ResponseEntity.ok().body(imagesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /images/:id} : get the "id" images.
     *
     * @param id the id of the images to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the images, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<Images> getImages(@PathVariable Long id) {
        log.debug("REST request to get Images : {}", id);
        Optional<Images> images = imagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(images);
    }

    /**
     * {@code DELETE  /images/:id} : delete the "id" images.
     *
     * @param id the id of the images to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImages(@PathVariable Long id) {
        log.debug("REST request to delete Images : {}", id);
        imagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
