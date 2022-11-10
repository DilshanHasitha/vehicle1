package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.MerchantRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Merchant}.
 */
@Service
@Transactional
public class MerchantService {

    private final Logger log = LoggerFactory.getLogger(MerchantService.class);

    private final MerchantRepository merchantRepository;

    private final CashBookService cashBookService;

    private final CashBookBalanceService cashBookBalanceService;

    private final ExpenseAccountService expenseAccountService;
    private final ExpenseAccountBalanceService expenseAccountBalanceService;

    public MerchantService(
        MerchantRepository merchantRepository,
        CashBookService cashBookService,
        CashBookBalanceService cashBookBalanceService,
        ExpenseAccountService expenseAccountService,
        ExpenseAccountBalanceService expenseAccountBalanceService
    ) {
        this.merchantRepository = merchantRepository;
        this.cashBookService = cashBookService;
        this.cashBookBalanceService = cashBookBalanceService;
        this.expenseAccountService = expenseAccountService;
        this.expenseAccountBalanceService = expenseAccountBalanceService;
    }

    /**
     * Save a merchant.
     *
     * @param merchant the entity to save.
     * @return the persisted entity.
     */
    public Merchant save(Merchant merchant) {
        log.debug("Request to save Merchant : {}", merchant);
        Merchant merchants = merchantRepository.save(merchant);

        CashBook cashBook = new CashBook();

        cashBook.setTransactionDate(java.time.LocalDate.now());
        cashBook.setTransactionDescription("Capital");
        cashBook.setTransactionAmountCR(BigDecimal.ZERO);
        cashBook.setTransactionAmountDR(BigDecimal.ZERO);
        cashBook.setTransactionBalance(BigDecimal.ZERO);
        cashBook.setMerchant(merchant);

        cashBookService.save(cashBook);

        CashBookBalance cashBookBalance = new CashBookBalance();

        cashBookBalance.setBalance(BigDecimal.ZERO);
        cashBookBalance.setMerchant(merchant);

        cashBookBalanceService.save(cashBookBalance);

        ExpenseAccount expenseAccount = new ExpenseAccount();
        expenseAccount.setTransactionDate(java.time.LocalDate.now());
        expenseAccount.setTransactionDescription("Capital");
        expenseAccount.setTransactionAmountCR(BigDecimal.ZERO);
        expenseAccount.setTransactionAmountDR(BigDecimal.ZERO);
        expenseAccount.setTransactionBalance(BigDecimal.ZERO);
        expenseAccount.setMerchant(merchant);

        expenseAccountService.save(expenseAccount);

        ExpenseAccountBalance expenseAccountBalance = new ExpenseAccountBalance();

        expenseAccountBalance.setBalance(BigDecimal.ZERO);
        expenseAccountBalance.setMerchant(merchant);

        expenseAccountBalanceService.save(expenseAccountBalance);

        cashBookBalanceService.save(cashBookBalance);

        return merchants;
    }

    /**
     * Update a merchant.
     *
     * @param merchant the entity to save.
     * @return the persisted entity.
     */
    public Merchant update(Merchant merchant) {
        log.debug("Request to update Merchant : {}", merchant);
        return merchantRepository.save(merchant);
    }

    /**
     * Partially update a merchant.
     *
     * @param merchant the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Merchant> partialUpdate(Merchant merchant) {
        log.debug("Request to partially update Merchant : {}", merchant);

        return merchantRepository
            .findById(merchant.getId())
            .map(existingMerchant -> {
                if (merchant.getCode() != null) {
                    existingMerchant.setCode(merchant.getCode());
                }
                if (merchant.getMerchantSecret() != null) {
                    existingMerchant.setMerchantSecret(merchant.getMerchantSecret());
                }
                if (merchant.getName() != null) {
                    existingMerchant.setName(merchant.getName());
                }
                if (merchant.getCreditLimit() != null) {
                    existingMerchant.setCreditLimit(merchant.getCreditLimit());
                }
                if (merchant.getIsActive() != null) {
                    existingMerchant.setIsActive(merchant.getIsActive());
                }
                if (merchant.getPhone() != null) {
                    existingMerchant.setPhone(merchant.getPhone());
                }
                if (merchant.getAddressLine1() != null) {
                    existingMerchant.setAddressLine1(merchant.getAddressLine1());
                }
                if (merchant.getAddressLine2() != null) {
                    existingMerchant.setAddressLine2(merchant.getAddressLine2());
                }
                if (merchant.getCity() != null) {
                    existingMerchant.setCity(merchant.getCity());
                }
                if (merchant.getCountry() != null) {
                    existingMerchant.setCountry(merchant.getCountry());
                }
                if (merchant.getPercentage() != null) {
                    existingMerchant.setPercentage(merchant.getPercentage());
                }
                if (merchant.getCreditScore() != null) {
                    existingMerchant.setCreditScore(merchant.getCreditScore());
                }
                if (merchant.getEmail() != null) {
                    existingMerchant.setEmail(merchant.getEmail());
                }
                if (merchant.getRating() != null) {
                    existingMerchant.setRating(merchant.getRating());
                }
                if (merchant.getLeadTime() != null) {
                    existingMerchant.setLeadTime(merchant.getLeadTime());
                }
                if (merchant.getIsSandBox() != null) {
                    existingMerchant.setIsSandBox(merchant.getIsSandBox());
                }
                if (merchant.getStoreDescription() != null) {
                    existingMerchant.setStoreDescription(merchant.getStoreDescription());
                }
                if (merchant.getStoreSecondaryDescription() != null) {
                    existingMerchant.setStoreSecondaryDescription(merchant.getStoreSecondaryDescription());
                }

                return existingMerchant;
            })
            .map(merchantRepository::save);
    }

    /**
     * Get all the merchants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Merchant> findAll(Pageable pageable) {
        log.debug("Request to get all Merchants");
        return merchantRepository.findAll(pageable);
    }

    /**
     * Get all the merchants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Merchant> findAllWithEagerRelationships(Pageable pageable) {
        return merchantRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one merchant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Merchant> findOne(Long id) {
        log.debug("Request to get Merchant : {}", id);
        return merchantRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the merchant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Merchant : {}", id);
        merchantRepository.deleteById(id);
    }
}
