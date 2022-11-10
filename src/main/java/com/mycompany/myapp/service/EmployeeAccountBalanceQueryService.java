package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.EmployeeAccountBalance;
import com.mycompany.myapp.repository.EmployeeAccountBalanceRepository;
import com.mycompany.myapp.service.criteria.EmployeeAccountBalanceCriteria;
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
 * Service for executing complex queries for {@link EmployeeAccountBalance} entities in the database.
 * The main input is a {@link EmployeeAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeAccountBalance} or a {@link Page} of {@link EmployeeAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeAccountBalanceQueryService extends QueryService<EmployeeAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountBalanceQueryService.class);

    private final EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    public EmployeeAccountBalanceQueryService(EmployeeAccountBalanceRepository employeeAccountBalanceRepository) {
        this.employeeAccountBalanceRepository = employeeAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link EmployeeAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeAccountBalance> findByCriteria(EmployeeAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeAccountBalance> specification = createSpecification(criteria);
        return employeeAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmployeeAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeAccountBalance> findByCriteria(EmployeeAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeAccountBalance> specification = createSpecification(criteria);
        return employeeAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployeeAccountBalance> specification = createSpecification(criteria);
        return employeeAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeAccountBalance> createSpecification(EmployeeAccountBalanceCriteria criteria) {
        Specification<EmployeeAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), EmployeeAccountBalance_.balance));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(EmployeeAccountBalance_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMerchantId(),
                            root -> root.join(EmployeeAccountBalance_.merchant, JoinType.LEFT).get(Merchant_.id)
                        )
                    );
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(EmployeeAccountBalance_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
