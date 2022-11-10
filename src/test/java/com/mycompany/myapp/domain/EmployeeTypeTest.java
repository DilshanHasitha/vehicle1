package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeType.class);
        EmployeeType employeeType1 = new EmployeeType();
        employeeType1.setId(1L);
        EmployeeType employeeType2 = new EmployeeType();
        employeeType2.setId(employeeType1.getId());
        assertThat(employeeType1).isEqualTo(employeeType2);
        employeeType2.setId(2L);
        assertThat(employeeType1).isNotEqualTo(employeeType2);
        employeeType1.setId(null);
        assertThat(employeeType1).isNotEqualTo(employeeType2);
    }
}
