package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Merchant;
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
public class MerchantRepositoryWithBagRelationshipsImpl implements MerchantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Merchant> fetchBagRelationships(Optional<Merchant> merchant) {
        return merchant.map(this::fetchExUsers);
    }

    @Override
    public Page<Merchant> fetchBagRelationships(Page<Merchant> merchants) {
        return new PageImpl<>(fetchBagRelationships(merchants.getContent()), merchants.getPageable(), merchants.getTotalElements());
    }

    @Override
    public List<Merchant> fetchBagRelationships(List<Merchant> merchants) {
        return Optional.of(merchants).map(this::fetchExUsers).orElse(Collections.emptyList());
    }

    Merchant fetchExUsers(Merchant result) {
        return entityManager
            .createQuery(
                "select merchant from Merchant merchant left join fetch merchant.exUsers where merchant is :merchant",
                Merchant.class
            )
            .setParameter("merchant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Merchant> fetchExUsers(List<Merchant> merchants) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, merchants.size()).forEach(index -> order.put(merchants.get(index).getId(), index));
        List<Merchant> result = entityManager
            .createQuery(
                "select distinct merchant from Merchant merchant left join fetch merchant.exUsers where merchant in :merchants",
                Merchant.class
            )
            .setParameter("merchants", merchants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
