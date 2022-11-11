package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseAccountRepository extends JpaRepository<ExpenseAccount, Long>, JpaSpecificationExecutor<ExpenseAccount> {
    Optional<List<ExpenseAccount>> findAllByMerchant_CodeAndTransactionDateAndExpense_ExpenseCode(
        String merchantCode,
        LocalDate date,
        String vehicle
    );
}
