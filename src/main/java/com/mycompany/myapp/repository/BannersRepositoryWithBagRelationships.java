package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Banners;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BannersRepositoryWithBagRelationships {
    Optional<Banners> fetchBagRelationships(Optional<Banners> banners);

    List<Banners> fetchBagRelationships(List<Banners> banners);

    Page<Banners> fetchBagRelationships(Page<Banners> banners);
}
