package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Banners;
import com.mycompany.myapp.repository.BannersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Banners}.
 */
@Service
@Transactional
public class BannersService {

    private final Logger log = LoggerFactory.getLogger(BannersService.class);

    private final BannersRepository bannersRepository;

    public BannersService(BannersRepository bannersRepository) {
        this.bannersRepository = bannersRepository;
    }

    /**
     * Save a banners.
     *
     * @param banners the entity to save.
     * @return the persisted entity.
     */
    public Banners save(Banners banners) {
        log.debug("Request to save Banners : {}", banners);
        return bannersRepository.save(banners);
    }

    /**
     * Update a banners.
     *
     * @param banners the entity to save.
     * @return the persisted entity.
     */
    public Banners update(Banners banners) {
        log.debug("Request to update Banners : {}", banners);
        return bannersRepository.save(banners);
    }

    /**
     * Partially update a banners.
     *
     * @param banners the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Banners> partialUpdate(Banners banners) {
        log.debug("Request to partially update Banners : {}", banners);

        return bannersRepository
            .findById(banners.getId())
            .map(existingBanners -> {
                if (banners.getCode() != null) {
                    existingBanners.setCode(banners.getCode());
                }
                if (banners.getHeading() != null) {
                    existingBanners.setHeading(banners.getHeading());
                }
                if (banners.getDescription() != null) {
                    existingBanners.setDescription(banners.getDescription());
                }
                if (banners.getLink() != null) {
                    existingBanners.setLink(banners.getLink());
                }

                return existingBanners;
            })
            .map(bannersRepository::save);
    }

    /**
     * Get all the banners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Banners> findAll(Pageable pageable) {
        log.debug("Request to get all Banners");
        return bannersRepository.findAll(pageable);
    }

    /**
     * Get all the banners with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Banners> findAllWithEagerRelationships(Pageable pageable) {
        return bannersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one banners by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Banners> findOne(Long id) {
        log.debug("Request to get Banners : {}", id);
        return bannersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the banners by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Banners : {}", id);
        bannersRepository.deleteById(id);
    }
}
