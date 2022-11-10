package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Banners;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.repository.ImagesRepository;
import com.mycompany.myapp.service.criteria.ImagesCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagesResourceIT {

    private static final String DEFAULT_IMGLOB_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_IMGLOB_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOW_RES_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOW_RES_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_BLOB_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagesMockMvc;

    private Images images;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createEntity(EntityManager em) {
        Images images = new Images()
            .imglobContentType(DEFAULT_IMGLOB_CONTENT_TYPE)
            .imageURL(DEFAULT_IMAGE_URL)
            .imageName(DEFAULT_IMAGE_NAME)
            .lowResURL(DEFAULT_LOW_RES_URL)
            .originalURL(DEFAULT_ORIGINAL_URL)
            .imageBlob(DEFAULT_IMAGE_BLOB)
            .imageBlobContentType(DEFAULT_IMAGE_BLOB_CONTENT_TYPE);
        return images;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createUpdatedEntity(EntityManager em) {
        Images images = new Images()
            .imglobContentType(UPDATED_IMGLOB_CONTENT_TYPE)
            .imageURL(UPDATED_IMAGE_URL)
            .imageName(UPDATED_IMAGE_NAME)
            .lowResURL(UPDATED_LOW_RES_URL)
            .originalURL(UPDATED_ORIGINAL_URL)
            .imageBlob(UPDATED_IMAGE_BLOB)
            .imageBlobContentType(UPDATED_IMAGE_BLOB_CONTENT_TYPE);
        return images;
    }

    @BeforeEach
    public void initTest() {
        images = createEntity(em);
    }

    @Test
    @Transactional
    void createImages() throws Exception {
        int databaseSizeBeforeCreate = imagesRepository.findAll().size();
        // Create the Images
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(images)))
            .andExpect(status().isCreated());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate + 1);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getImglobContentType()).isEqualTo(DEFAULT_IMGLOB_CONTENT_TYPE);
        assertThat(testImages.getImageURL()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testImages.getImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
        assertThat(testImages.getLowResURL()).isEqualTo(DEFAULT_LOW_RES_URL);
        assertThat(testImages.getOriginalURL()).isEqualTo(DEFAULT_ORIGINAL_URL);
        assertThat(testImages.getImageBlob()).isEqualTo(DEFAULT_IMAGE_BLOB);
        assertThat(testImages.getImageBlobContentType()).isEqualTo(DEFAULT_IMAGE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createImagesWithExistingId() throws Exception {
        // Create the Images with an existing ID
        images.setId(1L);

        int databaseSizeBeforeCreate = imagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(images)))
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].imglobContentType").value(hasItem(DEFAULT_IMGLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME)))
            .andExpect(jsonPath("$.[*].lowResURL").value(hasItem(DEFAULT_LOW_RES_URL)))
            .andExpect(jsonPath("$.[*].originalURL").value(hasItem(DEFAULT_ORIGINAL_URL)))
            .andExpect(jsonPath("$.[*].imageBlobContentType").value(hasItem(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB))));
    }

    @Test
    @Transactional
    void getImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get the images
        restImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, images.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(images.getId().intValue()))
            .andExpect(jsonPath("$.imglobContentType").value(DEFAULT_IMGLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageURL").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.imageName").value(DEFAULT_IMAGE_NAME))
            .andExpect(jsonPath("$.lowResURL").value(DEFAULT_LOW_RES_URL))
            .andExpect(jsonPath("$.originalURL").value(DEFAULT_ORIGINAL_URL))
            .andExpect(jsonPath("$.imageBlobContentType").value(DEFAULT_IMAGE_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageBlob").value(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB)));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        Long id = images.getId();

        defaultImagesShouldBeFound("id.equals=" + id);
        defaultImagesShouldNotBeFound("id.notEquals=" + id);

        defaultImagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.greaterThan=" + id);

        defaultImagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByImglobContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imglobContentType equals to DEFAULT_IMGLOB_CONTENT_TYPE
        defaultImagesShouldBeFound("imglobContentType.equals=" + DEFAULT_IMGLOB_CONTENT_TYPE);

        // Get all the imagesList where imglobContentType equals to UPDATED_IMGLOB_CONTENT_TYPE
        defaultImagesShouldNotBeFound("imglobContentType.equals=" + UPDATED_IMGLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImglobContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imglobContentType in DEFAULT_IMGLOB_CONTENT_TYPE or UPDATED_IMGLOB_CONTENT_TYPE
        defaultImagesShouldBeFound("imglobContentType.in=" + DEFAULT_IMGLOB_CONTENT_TYPE + "," + UPDATED_IMGLOB_CONTENT_TYPE);

        // Get all the imagesList where imglobContentType equals to UPDATED_IMGLOB_CONTENT_TYPE
        defaultImagesShouldNotBeFound("imglobContentType.in=" + UPDATED_IMGLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImglobContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imglobContentType is not null
        defaultImagesShouldBeFound("imglobContentType.specified=true");

        // Get all the imagesList where imglobContentType is null
        defaultImagesShouldNotBeFound("imglobContentType.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImglobContentTypeContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imglobContentType contains DEFAULT_IMGLOB_CONTENT_TYPE
        defaultImagesShouldBeFound("imglobContentType.contains=" + DEFAULT_IMGLOB_CONTENT_TYPE);

        // Get all the imagesList where imglobContentType contains UPDATED_IMGLOB_CONTENT_TYPE
        defaultImagesShouldNotBeFound("imglobContentType.contains=" + UPDATED_IMGLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImglobContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imglobContentType does not contain DEFAULT_IMGLOB_CONTENT_TYPE
        defaultImagesShouldNotBeFound("imglobContentType.doesNotContain=" + DEFAULT_IMGLOB_CONTENT_TYPE);

        // Get all the imagesList where imglobContentType does not contain UPDATED_IMGLOB_CONTENT_TYPE
        defaultImagesShouldBeFound("imglobContentType.doesNotContain=" + UPDATED_IMGLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImageURLIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageURL equals to DEFAULT_IMAGE_URL
        defaultImagesShouldBeFound("imageURL.equals=" + DEFAULT_IMAGE_URL);

        // Get all the imagesList where imageURL equals to UPDATED_IMAGE_URL
        defaultImagesShouldNotBeFound("imageURL.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllImagesByImageURLIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageURL in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultImagesShouldBeFound("imageURL.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the imagesList where imageURL equals to UPDATED_IMAGE_URL
        defaultImagesShouldNotBeFound("imageURL.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllImagesByImageURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageURL is not null
        defaultImagesShouldBeFound("imageURL.specified=true");

        // Get all the imagesList where imageURL is null
        defaultImagesShouldNotBeFound("imageURL.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImageURLContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageURL contains DEFAULT_IMAGE_URL
        defaultImagesShouldBeFound("imageURL.contains=" + DEFAULT_IMAGE_URL);

        // Get all the imagesList where imageURL contains UPDATED_IMAGE_URL
        defaultImagesShouldNotBeFound("imageURL.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllImagesByImageURLNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageURL does not contain DEFAULT_IMAGE_URL
        defaultImagesShouldNotBeFound("imageURL.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the imagesList where imageURL does not contain UPDATED_IMAGE_URL
        defaultImagesShouldBeFound("imageURL.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllImagesByImageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageName equals to DEFAULT_IMAGE_NAME
        defaultImagesShouldBeFound("imageName.equals=" + DEFAULT_IMAGE_NAME);

        // Get all the imagesList where imageName equals to UPDATED_IMAGE_NAME
        defaultImagesShouldNotBeFound("imageName.equals=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByImageNameIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageName in DEFAULT_IMAGE_NAME or UPDATED_IMAGE_NAME
        defaultImagesShouldBeFound("imageName.in=" + DEFAULT_IMAGE_NAME + "," + UPDATED_IMAGE_NAME);

        // Get all the imagesList where imageName equals to UPDATED_IMAGE_NAME
        defaultImagesShouldNotBeFound("imageName.in=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByImageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageName is not null
        defaultImagesShouldBeFound("imageName.specified=true");

        // Get all the imagesList where imageName is null
        defaultImagesShouldNotBeFound("imageName.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImageNameContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageName contains DEFAULT_IMAGE_NAME
        defaultImagesShouldBeFound("imageName.contains=" + DEFAULT_IMAGE_NAME);

        // Get all the imagesList where imageName contains UPDATED_IMAGE_NAME
        defaultImagesShouldNotBeFound("imageName.contains=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByImageNameNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageName does not contain DEFAULT_IMAGE_NAME
        defaultImagesShouldNotBeFound("imageName.doesNotContain=" + DEFAULT_IMAGE_NAME);

        // Get all the imagesList where imageName does not contain UPDATED_IMAGE_NAME
        defaultImagesShouldBeFound("imageName.doesNotContain=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByLowResURLIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lowResURL equals to DEFAULT_LOW_RES_URL
        defaultImagesShouldBeFound("lowResURL.equals=" + DEFAULT_LOW_RES_URL);

        // Get all the imagesList where lowResURL equals to UPDATED_LOW_RES_URL
        defaultImagesShouldNotBeFound("lowResURL.equals=" + UPDATED_LOW_RES_URL);
    }

    @Test
    @Transactional
    void getAllImagesByLowResURLIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lowResURL in DEFAULT_LOW_RES_URL or UPDATED_LOW_RES_URL
        defaultImagesShouldBeFound("lowResURL.in=" + DEFAULT_LOW_RES_URL + "," + UPDATED_LOW_RES_URL);

        // Get all the imagesList where lowResURL equals to UPDATED_LOW_RES_URL
        defaultImagesShouldNotBeFound("lowResURL.in=" + UPDATED_LOW_RES_URL);
    }

    @Test
    @Transactional
    void getAllImagesByLowResURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lowResURL is not null
        defaultImagesShouldBeFound("lowResURL.specified=true");

        // Get all the imagesList where lowResURL is null
        defaultImagesShouldNotBeFound("lowResURL.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByLowResURLContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lowResURL contains DEFAULT_LOW_RES_URL
        defaultImagesShouldBeFound("lowResURL.contains=" + DEFAULT_LOW_RES_URL);

        // Get all the imagesList where lowResURL contains UPDATED_LOW_RES_URL
        defaultImagesShouldNotBeFound("lowResURL.contains=" + UPDATED_LOW_RES_URL);
    }

    @Test
    @Transactional
    void getAllImagesByLowResURLNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lowResURL does not contain DEFAULT_LOW_RES_URL
        defaultImagesShouldNotBeFound("lowResURL.doesNotContain=" + DEFAULT_LOW_RES_URL);

        // Get all the imagesList where lowResURL does not contain UPDATED_LOW_RES_URL
        defaultImagesShouldBeFound("lowResURL.doesNotContain=" + UPDATED_LOW_RES_URL);
    }

    @Test
    @Transactional
    void getAllImagesByOriginalURLIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where originalURL equals to DEFAULT_ORIGINAL_URL
        defaultImagesShouldBeFound("originalURL.equals=" + DEFAULT_ORIGINAL_URL);

        // Get all the imagesList where originalURL equals to UPDATED_ORIGINAL_URL
        defaultImagesShouldNotBeFound("originalURL.equals=" + UPDATED_ORIGINAL_URL);
    }

    @Test
    @Transactional
    void getAllImagesByOriginalURLIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where originalURL in DEFAULT_ORIGINAL_URL or UPDATED_ORIGINAL_URL
        defaultImagesShouldBeFound("originalURL.in=" + DEFAULT_ORIGINAL_URL + "," + UPDATED_ORIGINAL_URL);

        // Get all the imagesList where originalURL equals to UPDATED_ORIGINAL_URL
        defaultImagesShouldNotBeFound("originalURL.in=" + UPDATED_ORIGINAL_URL);
    }

    @Test
    @Transactional
    void getAllImagesByOriginalURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where originalURL is not null
        defaultImagesShouldBeFound("originalURL.specified=true");

        // Get all the imagesList where originalURL is null
        defaultImagesShouldNotBeFound("originalURL.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByOriginalURLContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where originalURL contains DEFAULT_ORIGINAL_URL
        defaultImagesShouldBeFound("originalURL.contains=" + DEFAULT_ORIGINAL_URL);

        // Get all the imagesList where originalURL contains UPDATED_ORIGINAL_URL
        defaultImagesShouldNotBeFound("originalURL.contains=" + UPDATED_ORIGINAL_URL);
    }

    @Test
    @Transactional
    void getAllImagesByOriginalURLNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where originalURL does not contain DEFAULT_ORIGINAL_URL
        defaultImagesShouldNotBeFound("originalURL.doesNotContain=" + DEFAULT_ORIGINAL_URL);

        // Get all the imagesList where originalURL does not contain UPDATED_ORIGINAL_URL
        defaultImagesShouldBeFound("originalURL.doesNotContain=" + UPDATED_ORIGINAL_URL);
    }

    @Test
    @Transactional
    void getAllImagesByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            imagesRepository.saveAndFlush(images);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        images.setEmployee(employee);
        imagesRepository.saveAndFlush(images);
        Long employeeId = employee.getId();

        // Get all the imagesList where employee equals to employeeId
        defaultImagesShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the imagesList where employee equals to (employeeId + 1)
        defaultImagesShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllImagesByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            imagesRepository.saveAndFlush(images);
            merchant = MerchantResourceIT.createEntity(em);
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        images.addMerchant(merchant);
        imagesRepository.saveAndFlush(images);
        Long merchantId = merchant.getId();

        // Get all the imagesList where merchant equals to merchantId
        defaultImagesShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the imagesList where merchant equals to (merchantId + 1)
        defaultImagesShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllImagesByBannersIsEqualToSomething() throws Exception {
        Banners banners;
        if (TestUtil.findAll(em, Banners.class).isEmpty()) {
            imagesRepository.saveAndFlush(images);
            banners = BannersResourceIT.createEntity(em);
        } else {
            banners = TestUtil.findAll(em, Banners.class).get(0);
        }
        em.persist(banners);
        em.flush();
        images.addBanners(banners);
        imagesRepository.saveAndFlush(images);
        Long bannersId = banners.getId();

        // Get all the imagesList where banners equals to bannersId
        defaultImagesShouldBeFound("bannersId.equals=" + bannersId);

        // Get all the imagesList where banners equals to (bannersId + 1)
        defaultImagesShouldNotBeFound("bannersId.equals=" + (bannersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagesShouldBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].imglobContentType").value(hasItem(DEFAULT_IMGLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME)))
            .andExpect(jsonPath("$.[*].lowResURL").value(hasItem(DEFAULT_LOW_RES_URL)))
            .andExpect(jsonPath("$.[*].originalURL").value(hasItem(DEFAULT_ORIGINAL_URL)))
            .andExpect(jsonPath("$.[*].imageBlobContentType").value(hasItem(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB))));

        // Check, that the count call also returns 1
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagesShouldNotBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImages() throws Exception {
        // Get the images
        restImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images
        Images updatedImages = imagesRepository.findById(images.getId()).get();
        // Disconnect from session so that the updates on updatedImages are not directly saved in db
        em.detach(updatedImages);
        updatedImages
            .imglobContentType(UPDATED_IMGLOB_CONTENT_TYPE)
            .imageURL(UPDATED_IMAGE_URL)
            .imageName(UPDATED_IMAGE_NAME)
            .lowResURL(UPDATED_LOW_RES_URL)
            .originalURL(UPDATED_ORIGINAL_URL)
            .imageBlob(UPDATED_IMAGE_BLOB)
            .imageBlobContentType(UPDATED_IMAGE_BLOB_CONTENT_TYPE);

        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getImglobContentType()).isEqualTo(UPDATED_IMGLOB_CONTENT_TYPE);
        assertThat(testImages.getImageURL()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testImages.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
        assertThat(testImages.getLowResURL()).isEqualTo(UPDATED_LOW_RES_URL);
        assertThat(testImages.getOriginalURL()).isEqualTo(UPDATED_ORIGINAL_URL);
        assertThat(testImages.getImageBlob()).isEqualTo(UPDATED_IMAGE_BLOB);
        assertThat(testImages.getImageBlobContentType()).isEqualTo(UPDATED_IMAGE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, images.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(images))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(images))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(images)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages.imageURL(UPDATED_IMAGE_URL).imageName(UPDATED_IMAGE_NAME).lowResURL(UPDATED_LOW_RES_URL);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getImglobContentType()).isEqualTo(DEFAULT_IMGLOB_CONTENT_TYPE);
        assertThat(testImages.getImageURL()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testImages.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
        assertThat(testImages.getLowResURL()).isEqualTo(UPDATED_LOW_RES_URL);
        assertThat(testImages.getOriginalURL()).isEqualTo(DEFAULT_ORIGINAL_URL);
        assertThat(testImages.getImageBlob()).isEqualTo(DEFAULT_IMAGE_BLOB);
        assertThat(testImages.getImageBlobContentType()).isEqualTo(DEFAULT_IMAGE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages
            .imglobContentType(UPDATED_IMGLOB_CONTENT_TYPE)
            .imageURL(UPDATED_IMAGE_URL)
            .imageName(UPDATED_IMAGE_NAME)
            .lowResURL(UPDATED_LOW_RES_URL)
            .originalURL(UPDATED_ORIGINAL_URL)
            .imageBlob(UPDATED_IMAGE_BLOB)
            .imageBlobContentType(UPDATED_IMAGE_BLOB_CONTENT_TYPE);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getImglobContentType()).isEqualTo(UPDATED_IMGLOB_CONTENT_TYPE);
        assertThat(testImages.getImageURL()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testImages.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
        assertThat(testImages.getLowResURL()).isEqualTo(UPDATED_LOW_RES_URL);
        assertThat(testImages.getOriginalURL()).isEqualTo(UPDATED_ORIGINAL_URL);
        assertThat(testImages.getImageBlob()).isEqualTo(UPDATED_IMAGE_BLOB);
        assertThat(testImages.getImageBlobContentType()).isEqualTo(UPDATED_IMAGE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, images.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(images))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(images))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(images)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeDelete = imagesRepository.findAll().size();

        // Delete the images
        restImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, images.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
