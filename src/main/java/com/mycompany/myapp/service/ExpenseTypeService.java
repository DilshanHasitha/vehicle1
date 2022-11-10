package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExpenseType;
import com.mycompany.myapp.repository.ExpenseTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExpenseType}.
 */
@Service
@Transactional
public class ExpenseTypeService {

    private final Logger log = LoggerFactory.getLogger(ExpenseTypeService.class);

    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeService(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    /**
     * Save a expenseType.
     *
     * @param expenseType the entity to save.
     * @return the persisted entity.
     */
    public ExpenseType save(ExpenseType expenseType) {
        log.debug("Request to save ExpenseType : {}", expenseType);
        return expenseTypeRepository.save(expenseType);
    }

    /**
     * Update a expenseType.
     *
     * @param expenseType the entity to save.
     * @return the persisted entity.
     */
    public ExpenseType update(ExpenseType expenseType) {
        log.debug("Request to update ExpenseType : {}", expenseType);
        return expenseTypeRepository.save(expenseType);
    }

    /**
     * Partially update a expenseType.
     *
     * @param expenseType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseType> partialUpdate(ExpenseType expenseType) {
        log.debug("Request to partially update ExpenseType : {}", expenseType);

        return expenseTypeRepository
            .findById(expenseType.getId())
            .map(existingExpenseType -> {
                if (expenseType.getCode() != null) {
                    existingExpenseType.setCode(expenseType.getCode());
                }
                if (expenseType.getName() != null) {
                    existingExpenseType.setName(expenseType.getName());
                }
                if (expenseType.getIsActive() != null) {
                    existingExpenseType.setIsActive(expenseType.getIsActive());
                }

                return existingExpenseType;
            })
            .map(expenseTypeRepository::save);
    }

    /**
     * Get all the expenseTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseType> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseTypes");
        return expenseTypeRepository.findAll(pageable);
    }

    /**
     * Get one expenseType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseType> findOne(Long id) {
        log.debug("Request to get ExpenseType : {}", id);
        return expenseTypeRepository.findById(id);
    }

    /**
     * Delete the expenseType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseType : {}", id);
        expenseTypeRepository.deleteById(id);
    }
}
