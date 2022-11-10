package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CashBookBalance;
import com.mycompany.myapp.domain.EmployeeAccountBalance;
import com.mycompany.myapp.domain.ExpenseAccount;
import com.mycompany.myapp.domain.ExpenseAccountBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseAccountRepository extends JpaRepository<ExpenseAccount, Long>, JpaSpecificationExecutor<ExpenseAccount> {}
