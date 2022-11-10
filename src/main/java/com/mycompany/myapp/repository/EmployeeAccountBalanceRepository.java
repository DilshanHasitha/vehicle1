package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmployeeAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeAccountBalanceRepository
    extends JpaRepository<EmployeeAccountBalance, Long>, JpaSpecificationExecutor<EmployeeAccountBalance> {}
