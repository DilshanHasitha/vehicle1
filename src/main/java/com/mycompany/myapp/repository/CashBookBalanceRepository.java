package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CashBookBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CashBookBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookBalanceRepository extends JpaRepository<CashBookBalance, Long>, JpaSpecificationExecutor<CashBookBalance> {
    Optional<CashBookBalance> findOneByMerchant_Code(String merchantCode);
}
