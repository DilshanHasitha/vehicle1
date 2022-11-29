package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.CashBookBalanceRepository;
import com.mycompany.myapp.repository.ExpenseAccountBalanceRepository;
import com.mycompany.myapp.repository.ExpenseAccountRepository;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.RequestTransDTO;
import com.mycompany.myapp.service.dto.TransactionDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    public ExpenseAccountService(
        ExpenseAccountRepository expenseAccountRepository,
        CashBookService cashBookService,
        CashBookBalanceRepository cashBookBalanceRepository,
        CashBookBalanceService cashBookBalanceService,
        ExpenseAccountBalanceService expenseAccountBalanceService,
        ExpenseAccountBalanceRepository expenseAccountBalanceRepository,
        VehicleService vehicleService,
        VehicleRepository vehicleRepository
    ) {
        this.expenseAccountRepository = expenseAccountRepository;
        this.cashBookService = cashBookService;
        this.cashBookBalanceRepository = cashBookBalanceRepository;
        this.cashBookBalanceService = cashBookBalanceService;
        this.expenseAccountBalanceService = expenseAccountBalanceService;
        this.expenseAccountBalanceRepository = expenseAccountBalanceRepository;
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Save a expenseAccount.
     *
     * @param expenseAccount the entity to save.
     * @return the persisted entity.
     */
    public ExpenseAccount save(ExpenseAccount expenseAccount) {
        log.debug("Request to save ExpenseAccount : {}", expenseAccount);
        ExpenseAccount account = new ExpenseAccount();

        if (!expenseAccount.getTransactionAmountCR().equals(BigDecimal.ZERO)) {
            CashBook cashBook = new CashBook();
            ExpenseAccount newExpenseAccount = new ExpenseAccount();

            newExpenseAccount.setTransactionAmountCR(expenseAccount.getTransactionAmountCR());
            newExpenseAccount.setTransactionAmountDR(BigDecimal.ZERO);
            newExpenseAccount.setTransactionBalance(expenseAccount.getTransactionBalance());
            newExpenseAccount.setTransactionDescription(expenseAccount.getTransactionDescription());
            newExpenseAccount.setExpense(expenseAccount.getExpense());
            newExpenseAccount.setMerchant(expenseAccount.getMerchant());
            newExpenseAccount.setTransactionDate(expenseAccount.getTransactionDate());

            cashBook.setTransactionDate(expenseAccount.getTransactionDate());
            cashBook.setTransactionDescription(expenseAccount.getTransactionDescription());
            cashBook.setTransactionType(expenseAccount.getTransactionType());
            cashBook.setMerchant(expenseAccount.getMerchant());

            cashBook.setTransactionAmountDR(expenseAccount.getTransactionAmountCR());
            cashBook.setTransactionAmountCR(BigDecimal.ZERO);
            cashBook.setTransactionBalance(expenseAccount.getTransactionAmountCR());

            cashBookService.save(cashBook);

            account = expenseAccountRepository.save(newExpenseAccount);
        }

        if (!expenseAccount.getTransactionAmountDR().equals(BigDecimal.ZERO)) {
            CashBook cashBook = new CashBook();

            ExpenseAccount newExpenseAccount = new ExpenseAccount();

            newExpenseAccount.setTransactionAmountCR(BigDecimal.ZERO);
            newExpenseAccount.setTransactionAmountDR(expenseAccount.getTransactionAmountDR());
            newExpenseAccount.setTransactionBalance(expenseAccount.getTransactionBalance());
            newExpenseAccount.setTransactionDescription(expenseAccount.getTransactionDescription());
            newExpenseAccount.setExpense(expenseAccount.getExpense());
            newExpenseAccount.setMerchant(expenseAccount.getMerchant());
            newExpenseAccount.setTransactionDate(expenseAccount.getTransactionDate());

            cashBook.setTransactionDate(expenseAccount.getTransactionDate());
            cashBook.setTransactionDescription(expenseAccount.getTransactionDescription());
            cashBook.setTransactionType(expenseAccount.getTransactionType());
            cashBook.setMerchant(expenseAccount.getMerchant());

            cashBook.setTransactionAmountCR(expenseAccount.getTransactionAmountDR());
            cashBook.setTransactionAmountDR(BigDecimal.ZERO);
            cashBook.setTransactionBalance(expenseAccount.getTransactionAmountDR());

            cashBookService.save(cashBook);
            account = expenseAccountRepository.save(newExpenseAccount);
        }

        if (cashBookBalanceRepository.findOneByMerchant_Code(expenseAccount.getMerchant().getCode()).isPresent()) {
            CashBookBalance cashBookBalance = cashBookBalanceRepository
                .findOneByMerchant_Code(expenseAccount.getMerchant().getCode())
                .get();
            cashBookBalance.setBalance(
                cashBookBalance.getBalance().add(expenseAccount.getTransactionAmountCR()).subtract(expenseAccount.getTransactionAmountDR())
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

        return account;
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

    public List<TransactionDTO> getTransaction(RequestTransDTO requestTransDTO) {
        log.debug("Request to update ExpenseAccount : {}", requestTransDTO);
        List<Vehicle> vehicles = vehicleRepository.findAllByMerchant_Code(requestTransDTO.getMerchantCode()).get();

        List<TransactionDTO> transactions = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (
                expenseAccountRepository
                    .findAllByMerchant_CodeAndTransactionDateAndExpense_ExpenseCode(
                        requestTransDTO.getMerchantCode(),
                        requestTransDTO.getDate(),
                        vehicle.getExpenceCode()
                    )
                    .isPresent()
            ) {
                List<ExpenseAccount> expenseAccounts = expenseAccountRepository
                    .findAllByMerchant_CodeAndTransactionDateAndExpense_ExpenseCode(
                        requestTransDTO.getMerchantCode(),
                        requestTransDTO.getDate(),
                        vehicle.getExpenceCode()
                    )
                    .get();
                TransactionDTO trn = new TransactionDTO();

                if (expenseAccounts.size() > 0) {
                    BigDecimal cr = BigDecimal.ZERO;
                    BigDecimal dr = BigDecimal.ZERO;
                    for (ExpenseAccount expenseAccountDetails : expenseAccounts) {
                        cr = cr.add(expenseAccountDetails.getTransactionAmountDR());
                        dr = dr.add(expenseAccountDetails.getTransactionAmountCR());
                    }
                    trn.setCr(cr);
                    trn.setDr(dr);
                    trn.setDate(requestTransDTO.getDate());
                    trn.setVehicleNo(vehicle.getExpenceCode());

                    transactions.add(trn);
                }
            }
        }
        return transactions;
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
