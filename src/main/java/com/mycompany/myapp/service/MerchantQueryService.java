package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.service.criteria.MerchantCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Merchant} entities in the database.
 * The main input is a {@link MerchantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Merchant} or a {@link Page} of {@link Merchant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MerchantQueryService extends QueryService<Merchant> {

    private final Logger log = LoggerFactory.getLogger(MerchantQueryService.class);

    private final MerchantRepository merchantRepository;

    public MerchantQueryService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    /**
     * Return a {@link List} of {@link Merchant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Merchant> findByCriteria(MerchantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Merchant> specification = createSpecification(criteria);
        return merchantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Merchant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Merchant> findByCriteria(MerchantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Merchant> specification = createSpecification(criteria);
        return merchantRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MerchantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Merchant> specification = createSpecification(criteria);
        return merchantRepository.count(specification);
    }

    /**
     * Function to convert {@link MerchantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Merchant> createSpecification(MerchantCriteria criteria) {
        Specification<Merchant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Merchant_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Merchant_.code));
            }
            if (criteria.getMerchantSecret() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMerchantSecret(), Merchant_.merchantSecret));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Merchant_.name));
            }
            if (criteria.getCreditLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditLimit(), Merchant_.creditLimit));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Merchant_.isActive));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Merchant_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), Merchant_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), Merchant_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Merchant_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Merchant_.country));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), Merchant_.percentage));
            }
            if (criteria.getCreditScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditScore(), Merchant_.creditScore));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Merchant_.email));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Merchant_.rating));
            }
            if (criteria.getLeadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeadTime(), Merchant_.leadTime));
            }
            if (criteria.getIsSandBox() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSandBox(), Merchant_.isSandBox));
            }
            if (criteria.getStoreDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreDescription(), Merchant_.storeDescription));
            }
            if (criteria.getStoreSecondaryDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStoreSecondaryDescription(), Merchant_.storeSecondaryDescription)
                    );
            }
            if (criteria.getVehicleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVehicleId(), root -> root.join(Merchant_.vehicles, JoinType.LEFT).get(Vehicle_.id))
                    );
            }
            if (criteria.getImagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImagesId(), root -> root.join(Merchant_.images, JoinType.LEFT).get(Images_.id))
                    );
            }
            if (criteria.getExUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExUserId(), root -> root.join(Merchant_.exUsers, JoinType.LEFT).get(ExUser_.id))
                    );
            }
            if (criteria.getEmployeeAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeAccountId(),
                            root -> root.join(Merchant_.employeeAccounts, JoinType.LEFT).get(EmployeeAccount_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
