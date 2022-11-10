package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.repository.ExUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExUser}.
 */
@Service
@Transactional
public class ExUserService {

    private final Logger log = LoggerFactory.getLogger(ExUserService.class);

    private final ExUserRepository exUserRepository;

    public ExUserService(ExUserRepository exUserRepository) {
        this.exUserRepository = exUserRepository;
    }

    /**
     * Save a exUser.
     *
     * @param exUser the entity to save.
     * @return the persisted entity.
     */
    public ExUser save(ExUser exUser) {
        log.debug("Request to save ExUser : {}", exUser);
        return exUserRepository.save(exUser);
    }

    /**
     * Update a exUser.
     *
     * @param exUser the entity to save.
     * @return the persisted entity.
     */
    public ExUser update(ExUser exUser) {
        log.debug("Request to update ExUser : {}", exUser);
        return exUserRepository.save(exUser);
    }

    /**
     * Partially update a exUser.
     *
     * @param exUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExUser> partialUpdate(ExUser exUser) {
        log.debug("Request to partially update ExUser : {}", exUser);

        return exUserRepository
            .findById(exUser.getId())
            .map(existingExUser -> {
                if (exUser.getLogin() != null) {
                    existingExUser.setLogin(exUser.getLogin());
                }
                if (exUser.getFirstName() != null) {
                    existingExUser.setFirstName(exUser.getFirstName());
                }
                if (exUser.getLastName() != null) {
                    existingExUser.setLastName(exUser.getLastName());
                }
                if (exUser.getEmail() != null) {
                    existingExUser.setEmail(exUser.getEmail());
                }
                if (exUser.getIsActive() != null) {
                    existingExUser.setIsActive(exUser.getIsActive());
                }
                if (exUser.getPhone() != null) {
                    existingExUser.setPhone(exUser.getPhone());
                }
                if (exUser.getAddressLine1() != null) {
                    existingExUser.setAddressLine1(exUser.getAddressLine1());
                }
                if (exUser.getAddressLine2() != null) {
                    existingExUser.setAddressLine2(exUser.getAddressLine2());
                }
                if (exUser.getCity() != null) {
                    existingExUser.setCity(exUser.getCity());
                }
                if (exUser.getCountry() != null) {
                    existingExUser.setCountry(exUser.getCountry());
                }
                if (exUser.getImage() != null) {
                    existingExUser.setImage(exUser.getImage());
                }
                if (exUser.getImageContentType() != null) {
                    existingExUser.setImageContentType(exUser.getImageContentType());
                }
                if (exUser.getUserLimit() != null) {
                    existingExUser.setUserLimit(exUser.getUserLimit());
                }
                if (exUser.getCreditScore() != null) {
                    existingExUser.setCreditScore(exUser.getCreditScore());
                }

                return existingExUser;
            })
            .map(exUserRepository::save);
    }

    /**
     * Get all the exUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExUser> findAll(Pageable pageable) {
        log.debug("Request to get all ExUsers");
        return exUserRepository.findAll(pageable);
    }

    /**
     * Get one exUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExUser> findOne(Long id) {
        log.debug("Request to get ExUser : {}", id);
        return exUserRepository.findById(id);
    }

    /**
     * Delete the exUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExUser : {}", id);
        exUserRepository.deleteById(id);
    }
}
