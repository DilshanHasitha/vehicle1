package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.criteria.VehicleCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vehicle} entities in the database.
 * The main input is a {@link VehicleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Vehicle} or a {@link Page} of {@link Vehicle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleQueryService extends QueryService<Vehicle> {

    private final Logger log = LoggerFactory.getLogger(VehicleQueryService.class);

    private final VehicleRepository vehicleRepository;

    public VehicleQueryService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Return a {@link List} of {@link Vehicle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Vehicle> findByCriteria(VehicleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Vehicle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vehicle> findByCriteria(VehicleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicle> createSpecification(VehicleCriteria criteria) {
        Specification<Vehicle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehicle_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Vehicle_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Vehicle_.name));
            }
            if (criteria.getRelatedUserLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelatedUserLogin(), Vehicle_.relatedUserLogin));
            }
            if (criteria.getExpenceCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpenceCode(), Vehicle_.expenceCode));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Vehicle_.isActive));
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMerchantId(), root -> root.join(Vehicle_.merchant, JoinType.LEFT).get(Merchant_.id))
                    );
            }
        }
        return specification;
    }
}
