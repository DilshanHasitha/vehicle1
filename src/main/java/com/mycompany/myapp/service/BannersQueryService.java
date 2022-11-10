package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Banners;
import com.mycompany.myapp.repository.BannersRepository;
import com.mycompany.myapp.service.criteria.BannersCriteria;
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
 * Service for executing complex queries for {@link Banners} entities in the database.
 * The main input is a {@link BannersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Banners} or a {@link Page} of {@link Banners} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BannersQueryService extends QueryService<Banners> {

    private final Logger log = LoggerFactory.getLogger(BannersQueryService.class);

    private final BannersRepository bannersRepository;

    public BannersQueryService(BannersRepository bannersRepository) {
        this.bannersRepository = bannersRepository;
    }

    /**
     * Return a {@link List} of {@link Banners} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Banners> findByCriteria(BannersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Banners> specification = createSpecification(criteria);
        return bannersRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Banners} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Banners> findByCriteria(BannersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Banners> specification = createSpecification(criteria);
        return bannersRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BannersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Banners> specification = createSpecification(criteria);
        return bannersRepository.count(specification);
    }

    /**
     * Function to convert {@link BannersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Banners> createSpecification(BannersCriteria criteria) {
        Specification<Banners> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Banners_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Banners_.code));
            }
            if (criteria.getHeading() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeading(), Banners_.heading));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Banners_.description));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Banners_.link));
            }
            if (criteria.getImagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImagesId(), root -> root.join(Banners_.images, JoinType.LEFT).get(Images_.id))
                    );
            }
        }
        return specification;
    }
}
