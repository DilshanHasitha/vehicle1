package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeAccountBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAccountBalance.class);
        EmployeeAccountBalance employeeAccountBalance1 = new EmployeeAccountBalance();
        employeeAccountBalance1.setId(1L);
        EmployeeAccountBalance employeeAccountBalance2 = new EmployeeAccountBalance();
        employeeAccountBalance2.setId(employeeAccountBalance1.getId());
        assertThat(employeeAccountBalance1).isEqualTo(employeeAccountBalance2);
        employeeAccountBalance2.setId(2L);
        assertThat(employeeAccountBalance1).isNotEqualTo(employeeAccountBalance2);
        employeeAccountBalance1.setId(null);
        assertThat(employeeAccountBalance1).isNotEqualTo(employeeAccountBalance2);
    }
}
