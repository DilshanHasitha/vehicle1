package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseAccountBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseAccountBalance.class);
        ExpenseAccountBalance expenseAccountBalance1 = new ExpenseAccountBalance();
        expenseAccountBalance1.setId(1L);
        ExpenseAccountBalance expenseAccountBalance2 = new ExpenseAccountBalance();
        expenseAccountBalance2.setId(expenseAccountBalance1.getId());
        assertThat(expenseAccountBalance1).isEqualTo(expenseAccountBalance2);
        expenseAccountBalance2.setId(2L);
        assertThat(expenseAccountBalance1).isNotEqualTo(expenseAccountBalance2);
        expenseAccountBalance1.setId(null);
        assertThat(expenseAccountBalance1).isNotEqualTo(expenseAccountBalance2);
    }
}
