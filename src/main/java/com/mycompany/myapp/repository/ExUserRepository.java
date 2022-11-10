package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExUserRepository extends JpaRepository<ExUser, Long>, JpaSpecificationExecutor<ExUser> {}
