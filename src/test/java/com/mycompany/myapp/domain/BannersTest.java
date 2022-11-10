package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BannersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banners.class);
        Banners banners1 = new Banners();
        banners1.setId(1L);
        Banners banners2 = new Banners();
        banners2.setId(banners1.getId());
        assertThat(banners1).isEqualTo(banners2);
        banners2.setId(2L);
        assertThat(banners1).isNotEqualTo(banners2);
        banners1.setId(null);
        assertThat(banners1).isNotEqualTo(banners2);
    }
}
