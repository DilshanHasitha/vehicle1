package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmployeeAccountBalance;
import com.mycompany.myapp.repository.EmployeeAccountBalanceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeAccountBalance}.
 */
@Service
@Transactional
public class EmployeeAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountBalanceService.class);

    private final EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    public EmployeeAccountBalanceService(EmployeeAccountBalanceRepository employeeAccountBalanceRepository) {
        this.employeeAccountBalanceRepository = employeeAccountBalanceRepository;
    }

    /**
     * Save a employeeAccountBalance.
     *
     * @param employeeAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAccountBalance save(EmployeeAccountBalance employeeAccountBalance) {
        log.debug("Request to save EmployeeAccountBalance : {}", employeeAccountBalance);
        return employeeAccountBalanceRepository.save(employeeAccountBalance);
    }

    /**
     * Update a employeeAccountBalance.
     *
     * @param employeeAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAccountBalance update(EmployeeAccountBalance employeeAccountBalance) {
        log.debug("Request to update EmployeeAccountBalance : {}", employeeAccountBalance);
        return employeeAccountBalanceRepository.save(employeeAccountBalance);
    }

    /**
     * Partially update a employeeAccountBalance.
     *
     * @param employeeAccountBalance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeAccountBalance> partialUpdate(EmployeeAccountBalance employeeAccountBalance) {
        log.debug("Request to partially update EmployeeAccountBalance : {}", employeeAccountBalance);

        return employeeAccountBalanceRepository
            .findById(employeeAccountBalance.getId())
            .map(existingEmployeeAccountBalance -> {
                if (employeeAccountBalance.getBalance() != null) {
                    existingEmployeeAccountBalance.setBalance(employeeAccountBalance.getBalance());
                }

                return existingEmployeeAccountBalance;
            })
            .map(employeeAccountBalanceRepository::save);
    }

    /**
     * Get all the employeeAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeAccountBalances");
        return employeeAccountBalanceRepository.findAll(pageable);
    }

    /**
     * Get one employeeAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeAccountBalance> findOne(Long id) {
        log.debug("Request to get EmployeeAccountBalance : {}", id);
        return employeeAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the employeeAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeAccountBalance : {}", id);
        employeeAccountBalanceRepository.deleteById(id);
    }
}
