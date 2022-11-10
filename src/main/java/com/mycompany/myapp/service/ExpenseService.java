package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.repository.ExpenseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Expense}.
 */
@Service
@Transactional
public class ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Save a expense.
     *
     * @param expense the entity to save.
     * @return the persisted entity.
     */
    public Expense save(Expense expense) {
        log.debug("Request to save Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    /**
     * Update a expense.
     *
     * @param expense the entity to save.
     * @return the persisted entity.
     */
    public Expense update(Expense expense) {
        log.debug("Request to update Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    /**
     * Partially update a expense.
     *
     * @param expense the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Expense> partialUpdate(Expense expense) {
        log.debug("Request to partially update Expense : {}", expense);

        return expenseRepository
            .findById(expense.getId())
            .map(existingExpense -> {
                if (expense.getExpenseCode() != null) {
                    existingExpense.setExpenseCode(expense.getExpenseCode());
                }
                if (expense.getExpenseName() != null) {
                    existingExpense.setExpenseName(expense.getExpenseName());
                }
                if (expense.getExpenseLimit() != null) {
                    existingExpense.setExpenseLimit(expense.getExpenseLimit());
                }
                if (expense.getIsActive() != null) {
                    existingExpense.setIsActive(expense.getIsActive());
                }

                return existingExpense;
            })
            .map(expenseRepository::save);
    }

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Expense> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expenseRepository.findAll(pageable);
    }

    /**
     * Get one expense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Expense> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id);
    }

    /**
     * Delete the expense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}
