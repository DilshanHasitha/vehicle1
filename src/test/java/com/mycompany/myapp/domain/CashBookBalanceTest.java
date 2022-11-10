package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashBookBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBookBalance.class);
        CashBookBalance cashBookBalance1 = new CashBookBalance();
        cashBookBalance1.setId(1L);
        CashBookBalance cashBookBalance2 = new CashBookBalance();
        cashBookBalance2.setId(cashBookBalance1.getId());
        assertThat(cashBookBalance1).isEqualTo(cashBookBalance2);
        cashBookBalance2.setId(2L);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
        cashBookBalance1.setId(null);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
    }
}
