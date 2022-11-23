package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.Expense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Expense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    Optional<List<Expense>> findAllByExpenseCode(String expenseCode);

    Expense findOneByExpenseCode(String code);
}
