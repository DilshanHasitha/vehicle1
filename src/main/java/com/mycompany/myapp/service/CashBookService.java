package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.repository.CashBookRepository;
import com.mycompany.myapp.service.dto.RequestTransDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CashBook}.
 */
@Service
@Transactional
public class CashBookService {

    private final Logger log = LoggerFactory.getLogger(CashBookService.class);

    private final CashBookRepository cashBookRepository;

    public CashBookService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    /**
     * Save a cashBook.
     *
     * @param cashBook the entity to save.
     * @return the persisted entity.
     */
    public CashBook save(CashBook cashBook) {
        log.debug("Request to save CashBook : {}", cashBook);
        return cashBookRepository.save(cashBook);
    }

    /**
     * Update a cashBook.
     *
     * @param cashBook the entity to save.
     * @return the persisted entity.
     */
    public CashBook update(CashBook cashBook) {
        log.debug("Request to update CashBook : {}", cashBook);
        return cashBookRepository.save(cashBook);
    }

    /**
     * Partially update a cashBook.
     *
     * @param cashBook the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CashBook> partialUpdate(CashBook cashBook) {
        log.debug("Request to partially update CashBook : {}", cashBook);

        return cashBookRepository
            .findById(cashBook.getId())
            .map(existingCashBook -> {
                if (cashBook.getTransactionDate() != null) {
                    existingCashBook.setTransactionDate(cashBook.getTransactionDate());
                }
                if (cashBook.getTransactionDescription() != null) {
                    existingCashBook.setTransactionDescription(cashBook.getTransactionDescription());
                }
                if (cashBook.getTransactionAmountDR() != null) {
                    existingCashBook.setTransactionAmountDR(cashBook.getTransactionAmountDR());
                }
                if (cashBook.getTransactionAmountCR() != null) {
                    existingCashBook.setTransactionAmountCR(cashBook.getTransactionAmountCR());
                }
                if (cashBook.getTransactionBalance() != null) {
                    existingCashBook.setTransactionBalance(cashBook.getTransactionBalance());
                }

                return existingCashBook;
            })
            .map(cashBookRepository::save);
    }

    /**
     * Get all the cashBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBook> findAll(Pageable pageable) {
        log.debug("Request to get all CashBooks");
        return cashBookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<List<CashBook>> findAllByDate(RequestTransDTO requestTransDTO) {
        log.debug("Request to get all CashBooks");
        return cashBookRepository.findAllByMerchant_CodeAndTransactionDate(requestTransDTO.getMerchantCode(), requestTransDTO.getDate());
    }

    /**
     * Get one cashBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBook> findOne(Long id) {
        log.debug("Request to get CashBook : {}", id);
        return cashBookRepository.findById(id);
    }

    /**
     * Delete the cashBook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBook : {}", id);
        cashBookRepository.deleteById(id);
    }
}
