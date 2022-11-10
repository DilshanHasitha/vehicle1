package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.repository.ImagesRepository;
import com.mycompany.myapp.service.criteria.ImagesCriteria;
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
 * Service for executing complex queries for {@link Images} entities in the database.
 * The main input is a {@link ImagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Images} or a {@link Page} of {@link Images} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagesQueryService extends QueryService<Images> {

    private final Logger log = LoggerFactory.getLogger(ImagesQueryService.class);

    private final ImagesRepository imagesRepository;

    public ImagesQueryService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    /**
     * Return a {@link List} of {@link Images} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Images> findByCriteria(ImagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Images} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Images> findByCriteria(ImagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Images> createSpecification(ImagesCriteria criteria) {
        Specification<Images> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Images_.id));
            }
            if (criteria.getImglobContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImglobContentType(), Images_.imglobContentType));
            }
            if (criteria.getImageURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageURL(), Images_.imageURL));
            }
            if (criteria.getImageName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageName(), Images_.imageName));
            }
            if (criteria.getLowResURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLowResURL(), Images_.lowResURL));
            }
            if (criteria.getOriginalURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOriginalURL(), Images_.originalURL));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmployeeId(), root -> root.join(Images_.employee, JoinType.LEFT).get(Employee_.id))
                    );
            }
            if (criteria.getMerchantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMerchantId(), root -> root.join(Images_.merchants, JoinType.LEFT).get(Merchant_.id))
                    );
            }
            if (criteria.getBannersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBannersId(), root -> root.join(Images_.banners, JoinType.LEFT).get(Banners_.id))
                    );
            }
        }
        return specification;
    }
}
