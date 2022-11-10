package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Merchant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MerchantRepositoryWithBagRelationships {
    Optional<Merchant> fetchBagRelationships(Optional<Merchant> merchant);

    List<Merchant> fetchBagRelationships(List<Merchant> merchants);

    Page<Merchant> fetchBagRelationships(Page<Merchant> merchants);
}
