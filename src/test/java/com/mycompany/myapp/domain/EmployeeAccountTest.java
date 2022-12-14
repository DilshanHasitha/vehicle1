package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAccount.class);
        EmployeeAccount employeeAccount1 = new EmployeeAccount();
        employeeAccount1.setId(1L);
        EmployeeAccount employeeAccount2 = new EmployeeAccount();
        employeeAccount2.setId(employeeAccount1.getId());
        assertThat(employeeAccount1).isEqualTo(employeeAccount2);
        employeeAccount2.setId(2L);
        assertThat(employeeAccount1).isNotEqualTo(employeeAccount2);
        employeeAccount1.setId(null);
        assertThat(employeeAccount1).isNotEqualTo(employeeAccount2);
    }
}
