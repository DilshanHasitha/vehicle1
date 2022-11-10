package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Banners;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class BannersRepositoryWithBagRelationshipsImpl implements BannersRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Banners> fetchBagRelationships(Optional<Banners> banners) {
        return banners.map(this::fetchImages);
    }

    @Override
    public Page<Banners> fetchBagRelationships(Page<Banners> banners) {
        return new PageImpl<>(fetchBagRelationships(banners.getContent()), banners.getPageable(), banners.getTotalElements());
    }

    @Override
    public List<Banners> fetchBagRelationships(List<Banners> banners) {
        return Optional.of(banners).map(this::fetchImages).orElse(Collections.emptyList());
    }

    Banners fetchImages(Banners result) {
        return entityManager
            .createQuery("select banners from Banners banners left join fetch banners.images where banners is :banners", Banners.class)
            .setParameter("banners", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Banners> fetchImages(List<Banners> banners) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, banners.size()).forEach(index -> order.put(banners.get(index).getId(), index));
        List<Banners> result = entityManager
            .createQuery(
                "select distinct banners from Banners banners left join fetch banners.images where banners in :banners",
                Banners.class
            )
            .setParameter("banners", banners)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
