package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TransactionType;
import com.mycompany.myapp.repository.TransactionTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeService {

    private final Logger log = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * Save a transactionType.
     *
     * @param transactionType the entity to save.
     * @return the persisted entity.
     */
    public TransactionType save(TransactionType transactionType) {
        log.debug("Request to save TransactionType : {}", transactionType);
        return transactionTypeRepository.save(transactionType);
    }

    /**
     * Update a transactionType.
     *
     * @param transactionType the entity to save.
     * @return the persisted entity.
     */
    public TransactionType update(TransactionType transactionType) {
        log.debug("Request to update TransactionType : {}", transactionType);
        return transactionTypeRepository.save(transactionType);
    }

    /**
     * Partially update a transactionType.
     *
     * @param transactionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionType> partialUpdate(TransactionType transactionType) {
        log.debug("Request to partially update TransactionType : {}", transactionType);

        return transactionTypeRepository
            .findById(transactionType.getId())
            .map(existingTransactionType -> {
                if (transactionType.getCode() != null) {
                    existingTransactionType.setCode(transactionType.getCode());
                }
                if (transactionType.getDescription() != null) {
                    existingTransactionType.setDescription(transactionType.getDescription());
                }
                if (transactionType.getIsActive() != null) {
                    existingTransactionType.setIsActive(transactionType.getIsActive());
                }

                return existingTransactionType;
            })
            .map(transactionTypeRepository::save);
    }

    /**
     * Get all the transactionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionType> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionTypes");
        return transactionTypeRepository.findAll(pageable);
    }

    /**
     * Get one transactionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionType> findOne(Long id) {
        log.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id);
    }

    /**
     * Delete the transactionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }
}
