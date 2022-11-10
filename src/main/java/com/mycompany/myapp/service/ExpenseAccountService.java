package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.domain.CashBookBalance;
import com.mycompany.myapp.domain.ExpenseAccount;
import com.mycompany.myapp.domain.ExpenseAccountBalance;
import com.mycompany.myapp.repository.CashBookBalanceRepository;
import com.mycompany.myapp.repository.ExpenseAccountBalanceRepository;
import com.mycompany.myapp.repository.ExpenseAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExpenseAccount}.
 */
@Service
@Transactional
public class ExpenseAccountService {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountService.class);

    private final ExpenseAccountRepository expenseAccountRepository;

    private final CashBookService cashBookService;

    private final CashBookBalanceRepository cashBookBalanceRepository;

    private final CashBookBalanceService cashBookBalanceService;

    private final ExpenseAccountBalanceService expenseAccountBalanceService;

    private final ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    public ExpenseAccountService(
        ExpenseAccountRepository expenseAccountRepository,
        CashBookService cashBookService,
        CashBookBalanceRepository cashBookBalanceRepository,
        CashBookBalanceService cashBookBalanceService,
        ExpenseAccountBalanceService expenseAccountBalanceService,
        ExpenseAccountBalanceRepository expenseAccountBalanceRepository
    ) {
        this.expenseAccountRepository = expenseAccountRepository;
        this.cashBookService = cashBookService;
        this.cashBookBalanceRepository = cashBookBalanceRepository;
        this.cashBookBalanceService = cashBookBalanceService;
        this.expenseAccountBalanceService = expenseAccountBalanceService;
        this.expenseAccountBalanceRepository = expenseAccountBalanceRepository;
    }

    /**
     * Save a expenseAccount.
     *
     * @param expenseAccount the entity to save.
     * @return the persisted entity.
     */
    public ExpenseAccount save(ExpenseAccount expenseAccount) {
        log.debug("Request to save ExpenseAccount : {}", expenseAccount);

        CashBook cashBook = new CashBook();

        cashBook.setTransactionDate(expenseAccount.getTransactionDate());
        cashBook.setTransactionDescription(expenseAccount.getTransactionDescription());

        cashBook.setTransactionAmountCR(expenseAccount.getTransactionAmountDR());
        cashBook.setTransactionAmountDR(expenseAccount.getTransactionAmountCR());
        cashBook.setTransactionBalance(expenseAccount.getTransactionBalance());
        cashBook.setMerchant(expenseAccount.getMerchant());
        cashBook.setTransactionType(expenseAccount.getTransactionType());

        cashBookService.save(cashBook);

        if (cashBookBalanceRepository.findOneByMerchant_Code(expenseAccount.getMerchant().getCode()).isPresent()) {
            CashBookBalance cashBookBalance = cashBookBalanceRepository
                .findOneByMerchant_Code(expenseAccount.getMerchant().getCode())
                .get();
            cashBookBalance.setBalance(
                cashBookBalance.getBalance().add(expenseAccount.getTransactionAmountDR()).subtract(expenseAccount.getTransactionAmountCR())
            );

            cashBookBalanceService.update(cashBookBalance);
        }

        if (expenseAccountBalanceRepository.findOneByMerchant_Code(expenseAccount.getMerchant().getCode()).isPresent()) {
            ExpenseAccountBalance expenseAccountBalance = expenseAccountBalanceRepository
                .findOneByMerchant_Code(expenseAccount.getMerchant().getCode())
                .get();
            expenseAccountBalance.setBalance(
                expenseAccountBalance
                    .getBalance()
                    .add(expenseAccount.getTransactionAmountDR())
                    .subtract(expenseAccount.getTransactionAmountCR())
            );

            expenseAccountBalanceService.update(expenseAccountBalance);
        }

        return expenseAccountRepository.save(expenseAccount);
    }

    /**
     * Update a expenseAccount.
     *
     * @param expenseAccount the entity to save.
     * @return the persisted entity.
     */
    public ExpenseAccount update(ExpenseAccount expenseAccount) {
        log.debug("Request to update ExpenseAccount : {}", expenseAccount);
        return expenseAccountRepository.save(expenseAccount);
    }

    /**
     * Partially update a expenseAccount.
     *
     * @param expenseAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseAccount> partialUpdate(ExpenseAccount expenseAccount) {
        log.debug("Request to partially update ExpenseAccount : {}", expenseAccount);

        return expenseAccountRepository
            .findById(expenseAccount.getId())
            .map(existingExpenseAccount -> {
                if (expenseAccount.getTransactionDate() != null) {
                    existingExpenseAccount.setTransactionDate(expenseAccount.getTransactionDate());
                }
                if (expenseAccount.getTransactionDescription() != null) {
                    existingExpenseAccount.setTransactionDescription(expenseAccount.getTransactionDescription());
                }
                if (expenseAccount.getTransactionAmountDR() != null) {
                    existingExpenseAccount.setTransactionAmountDR(expenseAccount.getTransactionAmountDR());
                }
                if (expenseAccount.getTransactionAmountCR() != null) {
                    existingExpenseAccount.setTransactionAmountCR(expenseAccount.getTransactionAmountCR());
                }
                if (expenseAccount.getTransactionBalance() != null) {
                    existingExpenseAccount.setTransactionBalance(expenseAccount.getTransactionBalance());
                }

                return existingExpenseAccount;
            })
            .map(expenseAccountRepository::save);
    }

    /**
     * Get all the expenseAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseAccount> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseAccounts");
        return expenseAccountRepository.findAll(pageable);
    }

    /**
     * Get one expenseAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseAccount> findOne(Long id) {
        log.debug("Request to get ExpenseAccount : {}", id);
        return expenseAccountRepository.findById(id);
    }

    /**
     * Delete the expenseAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseAccount : {}", id);
        expenseAccountRepository.deleteById(id);
    }
}