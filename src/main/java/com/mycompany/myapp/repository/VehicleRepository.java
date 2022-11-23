package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vehicle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    Optional<List<Vehicle>> findAllByMerchant_Code(String code);
    Optional<Vehicle> findOneByName(String name);
}
