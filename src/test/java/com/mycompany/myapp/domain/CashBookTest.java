package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashBookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBook.class);
        CashBook cashBook1 = new CashBook();
        cashBook1.setId(1L);
        CashBook cashBook2 = new CashBook();
        cashBook2.setId(cashBook1.getId());
        assertThat(cashBook1).isEqualTo(cashBook2);
        cashBook2.setId(2L);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
        cashBook1.setId(null);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
    }
}
