package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.repository.ImagesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Images}.
 */
@Service
@Transactional
public class ImagesService {

    private final Logger log = LoggerFactory.getLogger(ImagesService.class);

    private final ImagesRepository imagesRepository;

    public ImagesService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    /**
     * Save a images.
     *
     * @param images the entity to save.
     * @return the persisted entity.
     */
    public Images save(Images images) {
        log.debug("Request to save Images : {}", images);
        return imagesRepository.save(images);
    }

    /**
     * Update a images.
     *
     * @param images the entity to save.
     * @return the persisted entity.
     */
    public Images update(Images images) {
        log.debug("Request to update Images : {}", images);
        return imagesRepository.save(images);
    }

    /**
     * Partially update a images.
     *
     * @param images the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Images> partialUpdate(Images images) {
        log.debug("Request to partially update Images : {}", images);

        return imagesRepository
            .findById(images.getId())
            .map(existingImages -> {
                if (images.getImglobContentType() != null) {
                    existingImages.setImglobContentType(images.getImglobContentType());
                }
                if (images.getImageURL() != null) {
                    existingImages.setImageURL(images.getImageURL());
                }
                if (images.getImageName() != null) {
                    existingImages.setImageName(images.getImageName());
                }
                if (images.getLowResURL() != null) {
                    existingImages.setLowResURL(images.getLowResURL());
                }
                if (images.getOriginalURL() != null) {
                    existingImages.setOriginalURL(images.getOriginalURL());
                }
                if (images.getImageBlob() != null) {
                    existingImages.setImageBlob(images.getImageBlob());
                }
                if (images.getImageBlobContentType() != null) {
                    existingImages.setImageBlobContentType(images.getImageBlobContentType());
                }

                return existingImages;
            })
            .map(imagesRepository::save);
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Images> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imagesRepository.findAll(pageable);
    }

    /**
     * Get one images by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Images> findOne(Long id) {
        log.debug("Request to get Images : {}", id);
        return imagesRepository.findById(id);
    }

    /**
     * Delete the images by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Images : {}", id);
        imagesRepository.deleteById(id);
    }
}
