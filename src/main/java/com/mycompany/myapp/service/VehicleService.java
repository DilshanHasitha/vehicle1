package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vehicle}.
 */
@Service
@Transactional
public class VehicleService {

    private final Logger log = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;
    private final ExUserService exUserService;

    private final ExpenseService expenseService;

    public VehicleService(VehicleRepository vehicleRepository, ExUserService exUserService, ExpenseService expenseService) {
        this.vehicleRepository = vehicleRepository;
        this.exUserService = exUserService;
        this.expenseService = expenseService;
    }

    /**
     * Save a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    public Vehicle save(Vehicle vehicle) {
        log.debug("Request to save Vehicle : {}", vehicle);
        Vehicle vehicle1 = vehicleRepository.save(vehicle);

        Expense expense = new Expense();

        if (vehicle.getExpenceCode().isEmpty()) {
            expense.setExpenseCode(vehicle.getName());
        } else {
            expense.setExpenseCode(vehicle.getExpenceCode());
        }
        expense.setExpenseLimit(BigDecimal.ZERO);
        expense.setExpenseName(vehicle.getName());

        expenseService.save(expense);

        return vehicle1;
    }

    /**
     * Update a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    public Vehicle update(Vehicle vehicle) {
        log.debug("Request to update Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    /**
     * Partially update a vehicle.
     *
     * @param vehicle the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vehicle> partialUpdate(Vehicle vehicle) {
        log.debug("Request to partially update Vehicle : {}", vehicle);

        return vehicleRepository
            .findById(vehicle.getId())
            .map(existingVehicle -> {
                if (vehicle.getCode() != null) {
                    existingVehicle.setCode(vehicle.getCode());
                }
                if (vehicle.getName() != null) {
                    existingVehicle.setName(vehicle.getName());
                }
                if (vehicle.getRelatedUserLogin() != null) {
                    existingVehicle.setRelatedUserLogin(vehicle.getRelatedUserLogin());
                }
                if (vehicle.getExpenceCode() != null) {
                    existingVehicle.setExpenceCode(vehicle.getExpenceCode());
                }
                if (vehicle.getIsActive() != null) {
                    existingVehicle.setIsActive(vehicle.getIsActive());
                }

                return existingVehicle;
            })
            .map(vehicleRepository::save);
    }

    /**
     * Get all the vehicles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Vehicle> findAll(Pageable pageable) {
        log.debug("Request to get all Vehicles");
        return vehicleRepository.findAll(pageable);
    }

    /**
     * Get one vehicle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vehicle> findOne(Long id) {
        log.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findById(id);
    }

    /**
     * Delete the vehicle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }
}
