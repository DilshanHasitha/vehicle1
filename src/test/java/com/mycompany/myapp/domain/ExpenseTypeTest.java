package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseType.class);
        ExpenseType expenseType1 = new ExpenseType();
        expenseType1.setId(1L);
        ExpenseType expenseType2 = new ExpenseType();
        expenseType2.setId(expenseType1.getId());
        assertThat(expenseType1).isEqualTo(expenseType2);
        expenseType2.setId(2L);
        assertThat(expenseType1).isNotEqualTo(expenseType2);
        expenseType1.setId(null);
        assertThat(expenseType1).isNotEqualTo(expenseType2);
    }
}
