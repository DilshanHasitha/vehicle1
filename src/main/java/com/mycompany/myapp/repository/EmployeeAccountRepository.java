package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmployeeAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, Long>, JpaSpecificationExecutor<EmployeeAccount> {}
