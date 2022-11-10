package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TransactionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long>, JpaSpecificationExecutor<TransactionType> {}
