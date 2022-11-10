package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExpenseAccount;
import com.mycompany.myapp.repository.ExpenseAccountRepository;
import com.mycompany.myapp.service.criteria.ExpenseAccountCriteria;
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
 * Service for executing complex queries for {@link ExpenseAccount} entities in the database.
 * The main input is a {@link ExpenseAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpenseAccount} or a {@link Page} of {@link ExpenseAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseAccountQueryService extends QueryService<ExpenseAccount> {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountQueryService.class);

    private final ExpenseAccountRepository expenseAccountRepository;

    public ExpenseAccountQueryService(ExpenseAccountRepository expenseAccountRepository) {
        this.expenseAccountRepository = expenseAccountRepository;
    }

    /**
     * Return a {@link List} of {@link ExpenseAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpenseAccount> findByCriteria(ExpenseAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpenseAccount> specification = createSpecification(criteria);
        return expenseAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExpenseAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseAccount> findByCriteria(ExpenseAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseAccount> specification = createSpecification(criteria);
        return expenseAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpenseAccount> specification = createSpecification(criteria);
        return expenseAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseAccount> createSpecification(ExpenseAccountCriteria criteria) {
        Specification<ExpenseAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpenseAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), ExpenseAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTransactionDescription(), ExpenseAccount_.transactionDescription)
                    );
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), ExpenseAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), ExpenseAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionBalance(), ExpenseAccount_.transactionBalance));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(ExpenseAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                        )
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMerchantId(),
                            root -> root.join(ExpenseAccount_.merchant, JoinType.LEFT).get(Merchant_.id)
                        )
                    );
            }
            if (criteria.getExpenseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExpenseId(),
                            root -> root.join(ExpenseAccount_.expense, JoinType.LEFT).get(Expense_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
