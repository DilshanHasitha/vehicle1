package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExpenseAccountBalance;
import com.mycompany.myapp.repository.ExpenseAccountBalanceRepository;
import com.mycompany.myapp.service.criteria.ExpenseAccountBalanceCriteria;
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
 * Service for executing complex queries for {@link ExpenseAccountBalance} entities in the database.
 * The main input is a {@link ExpenseAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpenseAccountBalance} or a {@link Page} of {@link ExpenseAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseAccountBalanceQueryService extends QueryService<ExpenseAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountBalanceQueryService.class);

    private final ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    public ExpenseAccountBalanceQueryService(ExpenseAccountBalanceRepository expenseAccountBalanceRepository) {
        this.expenseAccountBalanceRepository = expenseAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link ExpenseAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpenseAccountBalance> findByCriteria(ExpenseAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpenseAccountBalance> specification = createSpecification(criteria);
        return expenseAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExpenseAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseAccountBalance> findByCriteria(ExpenseAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseAccountBalance> specification = createSpecification(criteria);
        return expenseAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpenseAccountBalance> specification = createSpecification(criteria);
        return expenseAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseAccountBalance> createSpecification(ExpenseAccountBalanceCriteria criteria) {
        Specification<ExpenseAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpenseAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), ExpenseAccountBalance_.balance));
            }
            if (criteria.getExpenseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExpenseId(),
                            root -> root.join(ExpenseAccountBalance_.expense, JoinType.LEFT).get(Expense_.id)
                        )
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMerchantId(),
                            root -> root.join(ExpenseAccountBalance_.merchant, JoinType.LEFT).get(Merchant_.id)
                        )
                    );
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(ExpenseAccountBalance_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
