package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.EmployeeAccount;
import com.mycompany.myapp.repository.EmployeeAccountRepository;
import com.mycompany.myapp.service.criteria.EmployeeAccountCriteria;
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
 * Service for executing complex queries for {@link EmployeeAccount} entities in the database.
 * The main input is a {@link EmployeeAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeAccount} or a {@link Page} of {@link EmployeeAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeAccountQueryService extends QueryService<EmployeeAccount> {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountQueryService.class);

    private final EmployeeAccountRepository employeeAccountRepository;

    public EmployeeAccountQueryService(EmployeeAccountRepository employeeAccountRepository) {
        this.employeeAccountRepository = employeeAccountRepository;
    }

    /**
     * Return a {@link List} of {@link EmployeeAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeAccount> findByCriteria(EmployeeAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeAccount> specification = createSpecification(criteria);
        return employeeAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmployeeAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeAccount> findByCriteria(EmployeeAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeAccount> specification = createSpecification(criteria);
        return employeeAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployeeAccount> specification = createSpecification(criteria);
        return employeeAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeAccount> createSpecification(EmployeeAccountCriteria criteria) {
        Specification<EmployeeAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), EmployeeAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTransactionDescription(), EmployeeAccount_.transactionDescription)
                    );
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), EmployeeAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), EmployeeAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionBalance(), EmployeeAccount_.transactionBalance));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(EmployeeAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                        )
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMerchantId(),
                            root -> root.join(EmployeeAccount_.merchant, JoinType.LEFT).get(Merchant_.id)
                        )
                    );
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(EmployeeAccount_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
