package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExpenseType;
import com.mycompany.myapp.repository.ExpenseTypeRepository;
import com.mycompany.myapp.service.criteria.ExpenseTypeCriteria;
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
 * Service for executing complex queries for {@link ExpenseType} entities in the database.
 * The main input is a {@link ExpenseTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpenseType} or a {@link Page} of {@link ExpenseType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseTypeQueryService extends QueryService<ExpenseType> {

    private final Logger log = LoggerFactory.getLogger(ExpenseTypeQueryService.class);

    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeQueryService(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ExpenseType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpenseType> findByCriteria(ExpenseTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpenseType> specification = createSpecification(criteria);
        return expenseTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExpenseType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseType> findByCriteria(ExpenseTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseType> specification = createSpecification(criteria);
        return expenseTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpenseType> specification = createSpecification(criteria);
        return expenseTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseType> createSpecification(ExpenseTypeCriteria criteria) {
        Specification<ExpenseType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpenseType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ExpenseType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ExpenseType_.name));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), ExpenseType_.isActive));
            }
        }
        return specification;
    }
}
