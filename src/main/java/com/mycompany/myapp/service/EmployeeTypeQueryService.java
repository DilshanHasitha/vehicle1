package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.EmployeeType;
import com.mycompany.myapp.repository.EmployeeTypeRepository;
import com.mycompany.myapp.service.criteria.EmployeeTypeCriteria;
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
 * Service for executing complex queries for {@link EmployeeType} entities in the database.
 * The main input is a {@link EmployeeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeType} or a {@link Page} of {@link EmployeeType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeTypeQueryService extends QueryService<EmployeeType> {

    private final Logger log = LoggerFactory.getLogger(EmployeeTypeQueryService.class);

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeTypeQueryService(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }

    /**
     * Return a {@link List} of {@link EmployeeType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeType> findByCriteria(EmployeeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeType> specification = createSpecification(criteria);
        return employeeTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmployeeType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeType> findByCriteria(EmployeeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeType> specification = createSpecification(criteria);
        return employeeTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployeeType> specification = createSpecification(criteria);
        return employeeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeType> createSpecification(EmployeeTypeCriteria criteria) {
        Specification<EmployeeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), EmployeeType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EmployeeType_.name));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EmployeeType_.isActive));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(EmployeeType_.employees, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
