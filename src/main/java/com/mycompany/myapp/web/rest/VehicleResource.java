package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.VehicleQueryService;
import com.mycompany.myapp.service.VehicleService;
import com.mycompany.myapp.service.criteria.VehicleCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Vehicle}.
 */
@RestController
@RequestMapping("/api")
public class VehicleResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);

    private static final String ENTITY_NAME = "vehicle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleService vehicleService;

    private final VehicleRepository vehicleRepository;

    private final VehicleQueryService vehicleQueryService;

    public VehicleResource(VehicleService vehicleService, VehicleRepository vehicleRepository, VehicleQueryService vehicleQueryService) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleQueryService = vehicleQueryService;
    }

    /**
     * {@code POST  /vehicles} : Create a new vehicle.
     *
     * @param vehicle the vehicle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicle, or with status {@code 400 (Bad Request)} if the vehicle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) throws URISyntaxException {
        log.debug("REST request to save Vehicle : {}", vehicle);
        if (vehicle.getId() != null) {
            throw new BadRequestAlertException("A new vehicle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vehicle result = vehicleService.save(vehicle);
        return ResponseEntity
            .created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicles/:id} : Updates an existing vehicle.
     *
     * @param id the id of the vehicle to save.
     * @param vehicle the vehicle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicle,
     * or with status {@code 400 (Bad Request)} if the vehicle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Vehicle vehicle
    ) throws URISyntaxException {
        log.debug("REST request to update Vehicle : {}, {}", id, vehicle);
        if (vehicle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vehicle result = vehicleService.update(vehicle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicles/:id} : Partial updates given fields of an existing vehicle, field will ignore if it is null
     *
     * @param id the id of the vehicle to save.
     * @param vehicle the vehicle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicle,
     * or with status {@code 400 (Bad Request)} if the vehicle is not valid,
     * or with status {@code 404 (Not Found)} if the vehicle is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vehicle> partialUpdateVehicle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Vehicle vehicle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehicle partially : {}, {}", id, vehicle);
        if (vehicle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vehicle> result = vehicleService.partialUpdate(vehicle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicle.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicles} : get all the vehicles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicles in body.
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(
        VehicleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Vehicles by criteria: {}", criteria);
        Page<Vehicle> page = vehicleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicles/count} : count all the vehicles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vehicles/count")
    public ResponseEntity<Long> countVehicles(VehicleCriteria criteria) {
        log.debug("REST request to count Vehicles by criteria: {}", criteria);
        return ResponseEntity.ok().body(vehicleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vehicles/:id} : get the "id" vehicle.
     *
     * @param id the id of the vehicle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long id) {
        log.debug("REST request to get Vehicle : {}", id);
        Optional<Vehicle> vehicle = vehicleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicle);
    }

    /**
     * {@code DELETE  /vehicles/:id} : delete the "id" vehicle.
     *
     * @param id the id of the vehicle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete Vehicle : {}", id);
        vehicleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
