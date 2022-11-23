package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.domain.Merchant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Merchant entity.
 *
 * When extending this class, extend MerchantRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MerchantRepository
    extends MerchantRepositoryWithBagRelationships, JpaRepository<Merchant, Long>, JpaSpecificationExecutor<Merchant> {
    default Optional<Merchant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Merchant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Merchant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Merchant findOneByCode(String code);
}
