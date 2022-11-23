package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    List<Employee> findAllByMerchant_Code(String merchantCode);

    Optional<Employee> findOneByPhone(String phone);

    Employee findAllByUser_login(String user);
}
