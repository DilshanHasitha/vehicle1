package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmployeeType;
import com.mycompany.myapp.repository.EmployeeTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeType}.
 */
@Service
@Transactional
public class EmployeeTypeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeTypeService.class);

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeTypeService(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }

    /**
     * Save a employeeType.
     *
     * @param employeeType the entity to save.
     * @return the persisted entity.
     */
    public EmployeeType save(EmployeeType employeeType) {
        log.debug("Request to save EmployeeType : {}", employeeType);
        return employeeTypeRepository.save(employeeType);
    }

    /**
     * Update a employeeType.
     *
     * @param employeeType the entity to save.
     * @return the persisted entity.
     */
    public EmployeeType update(EmployeeType employeeType) {
        log.debug("Request to update EmployeeType : {}", employeeType);
        return employeeTypeRepository.save(employeeType);
    }

    /**
     * Partially update a employeeType.
     *
     * @param employeeType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeType> partialUpdate(EmployeeType employeeType) {
        log.debug("Request to partially update EmployeeType : {}", employeeType);

        return employeeTypeRepository
            .findById(employeeType.getId())
            .map(existingEmployeeType -> {
                if (employeeType.getCode() != null) {
                    existingEmployeeType.setCode(employeeType.getCode());
                }
                if (employeeType.getName() != null) {
                    existingEmployeeType.setName(employeeType.getName());
                }
                if (employeeType.getIsActive() != null) {
                    existingEmployeeType.setIsActive(employeeType.getIsActive());
                }

                return existingEmployeeType;
            })
            .map(employeeTypeRepository::save);
    }

    /**
     * Get all the employeeTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeType> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeTypes");
        return employeeTypeRepository.findAll(pageable);
    }

    /**
     * Get one employeeType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeType> findOne(Long id) {
        log.debug("Request to get EmployeeType : {}", id);
        return employeeTypeRepository.findById(id);
    }

    /**
     * Delete the employeeType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeType : {}", id);
        employeeTypeRepository.deleteById(id);
    }
}
