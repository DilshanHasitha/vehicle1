package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CashBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CashBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long>, JpaSpecificationExecutor<CashBook> {}
