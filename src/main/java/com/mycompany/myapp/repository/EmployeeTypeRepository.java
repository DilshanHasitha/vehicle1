package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmployeeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long>, JpaSpecificationExecutor<EmployeeType> {}
