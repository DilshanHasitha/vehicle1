package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.repository.ExUserRepository;
import com.mycompany.myapp.service.criteria.ExUserCriteria;
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
 * Service for executing complex queries for {@link ExUser} entities in the database.
 * The main input is a {@link ExUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExUser} or a {@link Page} of {@link ExUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExUserQueryService extends QueryService<ExUser> {

    private final Logger log = LoggerFactory.getLogger(ExUserQueryService.class);

    private final ExUserRepository exUserRepository;

    public ExUserQueryService(ExUserRepository exUserRepository) {
        this.exUserRepository = exUserRepository;
    }

    /**
     * Return a {@link List} of {@link ExUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExUser> findByCriteria(ExUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExUser> findByCriteria(ExUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ExUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExUser> createSpecification(ExUserCriteria criteria) {
        Specification<ExUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExUser_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), ExUser_.login));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), ExUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), ExUser_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), ExUser_.email));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), ExUser_.isActive));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), ExUser_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), ExUser_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), ExUser_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), ExUser_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), ExUser_.country));
            }
            if (criteria.getUserLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserLimit(), ExUser_.userLimit));
            }
            if (criteria.getCreditScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditScore(), ExUser_.creditScore));
            }
            if (criteria.getRelatedUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRelatedUserId(), root -> root.join(ExUser_.relatedUser, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMerchantId(), root -> root.join(ExUser_.merchants, JoinType.LEFT).get(Merchant_.id))
                    );
            }
        }
        return specification;
    }
}
