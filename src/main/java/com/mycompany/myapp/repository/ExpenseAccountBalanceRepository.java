package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExpenseAccountBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseAccountBalanceRepository
    extends JpaRepository<ExpenseAccountBalance, Long>, JpaSpecificationExecutor<ExpenseAccountBalance> {
    Optional<ExpenseAccountBalance> findOneByMerchant_Code(String merchantCode);
}
