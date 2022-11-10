package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Banners;
import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.repository.BannersRepository;
import com.mycompany.myapp.service.BannersService;
import com.mycompany.myapp.service.criteria.BannersCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BannersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BannersResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_HEADING = "AAAAAAAAAA";
    private static final String UPDATED_HEADING = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/banners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BannersRepository bannersRepository;

    @Mock
    private BannersRepository bannersRepositoryMock;

    @Mock
    private BannersService bannersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBannersMockMvc;

    private Banners banners;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banners createEntity(EntityManager em) {
        Banners banners = new Banners().code(DEFAULT_CODE).heading(DEFAULT_HEADING).description(DEFAULT_DESCRIPTION).link(DEFAULT_LINK);
        return banners;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banners createUpdatedEntity(EntityManager em) {
        Banners banners = new Banners().code(UPDATED_CODE).heading(UPDATED_HEADING).description(UPDATED_DESCRIPTION).link(UPDATED_LINK);
        return banners;
    }

    @BeforeEach
    public void initTest() {
        banners = createEntity(em);
    }

    @Test
    @Transactional
    void createBanners() throws Exception {
        int databaseSizeBeforeCreate = bannersRepository.findAll().size();
        // Create the Banners
        restBannersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banners)))
            .andExpect(status().isCreated());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeCreate + 1);
        Banners testBanners = bannersList.get(bannersList.size() - 1);
        assertThat(testBanners.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBanners.getHeading()).isEqualTo(DEFAULT_HEADING);
        assertThat(testBanners.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBanners.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    void createBannersWithExistingId() throws Exception {
        // Create the Banners with an existing ID
        banners.setId(1L);

        int databaseSizeBeforeCreate = bannersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banners)))
            .andExpect(status().isBadRequest());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBanners() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList
        restBannersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banners.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBannersWithEagerRelationshipsIsEnabled() throws Exception {
        when(bannersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBannersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bannersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBannersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bannersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBannersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bannersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBanners() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get the banners
        restBannersMockMvc
            .perform(get(ENTITY_API_URL_ID, banners.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banners.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.heading").value(DEFAULT_HEADING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK));
    }

    @Test
    @Transactional
    void getBannersByIdFiltering() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        Long id = banners.getId();

        defaultBannersShouldBeFound("id.equals=" + id);
        defaultBannersShouldNotBeFound("id.notEquals=" + id);

        defaultBannersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBannersShouldNotBeFound("id.greaterThan=" + id);

        defaultBannersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBannersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBannersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where code equals to DEFAULT_CODE
        defaultBannersShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the bannersList where code equals to UPDATED_CODE
        defaultBannersShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBannersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBannersShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the bannersList where code equals to UPDATED_CODE
        defaultBannersShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBannersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where code is not null
        defaultBannersShouldBeFound("code.specified=true");

        // Get all the bannersList where code is null
        defaultBannersShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBannersByCodeContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where code contains DEFAULT_CODE
        defaultBannersShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the bannersList where code contains UPDATED_CODE
        defaultBannersShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBannersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where code does not contain DEFAULT_CODE
        defaultBannersShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the bannersList where code does not contain UPDATED_CODE
        defaultBannersShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBannersByHeadingIsEqualToSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where heading equals to DEFAULT_HEADING
        defaultBannersShouldBeFound("heading.equals=" + DEFAULT_HEADING);

        // Get all the bannersList where heading equals to UPDATED_HEADING
        defaultBannersShouldNotBeFound("heading.equals=" + UPDATED_HEADING);
    }

    @Test
    @Transactional
    void getAllBannersByHeadingIsInShouldWork() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where heading in DEFAULT_HEADING or UPDATED_HEADING
        defaultBannersShouldBeFound("heading.in=" + DEFAULT_HEADING + "," + UPDATED_HEADING);

        // Get all the bannersList where heading equals to UPDATED_HEADING
        defaultBannersShouldNotBeFound("heading.in=" + UPDATED_HEADING);
    }

    @Test
    @Transactional
    void getAllBannersByHeadingIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where heading is not null
        defaultBannersShouldBeFound("heading.specified=true");

        // Get all the bannersList where heading is null
        defaultBannersShouldNotBeFound("heading.specified=false");
    }

    @Test
    @Transactional
    void getAllBannersByHeadingContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where heading contains DEFAULT_HEADING
        defaultBannersShouldBeFound("heading.contains=" + DEFAULT_HEADING);

        // Get all the bannersList where heading contains UPDATED_HEADING
        defaultBannersShouldNotBeFound("heading.contains=" + UPDATED_HEADING);
    }

    @Test
    @Transactional
    void getAllBannersByHeadingNotContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where heading does not contain DEFAULT_HEADING
        defaultBannersShouldNotBeFound("heading.doesNotContain=" + DEFAULT_HEADING);

        // Get all the bannersList where heading does not contain UPDATED_HEADING
        defaultBannersShouldBeFound("heading.doesNotContain=" + UPDATED_HEADING);
    }

    @Test
    @Transactional
    void getAllBannersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where description equals to DEFAULT_DESCRIPTION
        defaultBannersShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the bannersList where description equals to UPDATED_DESCRIPTION
        defaultBannersShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBannersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBannersShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the bannersList where description equals to UPDATED_DESCRIPTION
        defaultBannersShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBannersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where description is not null
        defaultBannersShouldBeFound("description.specified=true");

        // Get all the bannersList where description is null
        defaultBannersShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBannersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where description contains DEFAULT_DESCRIPTION
        defaultBannersShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the bannersList where description contains UPDATED_DESCRIPTION
        defaultBannersShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBannersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where description does not contain DEFAULT_DESCRIPTION
        defaultBannersShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the bannersList where description does not contain UPDATED_DESCRIPTION
        defaultBannersShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBannersByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where link equals to DEFAULT_LINK
        defaultBannersShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the bannersList where link equals to UPDATED_LINK
        defaultBannersShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllBannersByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where link in DEFAULT_LINK or UPDATED_LINK
        defaultBannersShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the bannersList where link equals to UPDATED_LINK
        defaultBannersShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllBannersByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where link is not null
        defaultBannersShouldBeFound("link.specified=true");

        // Get all the bannersList where link is null
        defaultBannersShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllBannersByLinkContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where link contains DEFAULT_LINK
        defaultBannersShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the bannersList where link contains UPDATED_LINK
        defaultBannersShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllBannersByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        // Get all the bannersList where link does not contain DEFAULT_LINK
        defaultBannersShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the bannersList where link does not contain UPDATED_LINK
        defaultBannersShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllBannersByImagesIsEqualToSomething() throws Exception {
        Images images;
        if (TestUtil.findAll(em, Images.class).isEmpty()) {
            bannersRepository.saveAndFlush(banners);
            images = ImagesResourceIT.createEntity(em);
        } else {
            images = TestUtil.findAll(em, Images.class).get(0);
        }
        em.persist(images);
        em.flush();
        banners.addImages(images);
        bannersRepository.saveAndFlush(banners);
        Long imagesId = images.getId();

        // Get all the bannersList where images equals to imagesId
        defaultBannersShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the bannersList where images equals to (imagesId + 1)
        defaultBannersShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBannersShouldBeFound(String filter) throws Exception {
        restBannersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banners.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));

        // Check, that the count call also returns 1
        restBannersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBannersShouldNotBeFound(String filter) throws Exception {
        restBannersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBannersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBanners() throws Exception {
        // Get the banners
        restBannersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBanners() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();

        // Update the banners
        Banners updatedBanners = bannersRepository.findById(banners.getId()).get();
        // Disconnect from session so that the updates on updatedBanners are not directly saved in db
        em.detach(updatedBanners);
        updatedBanners.code(UPDATED_CODE).heading(UPDATED_HEADING).description(UPDATED_DESCRIPTION).link(UPDATED_LINK);

        restBannersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBanners.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBanners))
            )
            .andExpect(status().isOk());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
        Banners testBanners = bannersList.get(bannersList.size() - 1);
        assertThat(testBanners.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBanners.getHeading()).isEqualTo(UPDATED_HEADING);
        assertThat(testBanners.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanners.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    void putNonExistingBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, banners.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(banners))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(banners))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banners)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBannersWithPatch() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();

        // Update the banners using partial update
        Banners partialUpdatedBanners = new Banners();
        partialUpdatedBanners.setId(banners.getId());

        partialUpdatedBanners.code(UPDATED_CODE);

        restBannersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanners.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanners))
            )
            .andExpect(status().isOk());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
        Banners testBanners = bannersList.get(bannersList.size() - 1);
        assertThat(testBanners.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBanners.getHeading()).isEqualTo(DEFAULT_HEADING);
        assertThat(testBanners.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBanners.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    void fullUpdateBannersWithPatch() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();

        // Update the banners using partial update
        Banners partialUpdatedBanners = new Banners();
        partialUpdatedBanners.setId(banners.getId());

        partialUpdatedBanners.code(UPDATED_CODE).heading(UPDATED_HEADING).description(UPDATED_DESCRIPTION).link(UPDATED_LINK);

        restBannersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanners.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanners))
            )
            .andExpect(status().isOk());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
        Banners testBanners = bannersList.get(bannersList.size() - 1);
        assertThat(testBanners.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBanners.getHeading()).isEqualTo(UPDATED_HEADING);
        assertThat(testBanners.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanners.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    void patchNonExistingBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, banners.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(banners))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(banners))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBanners() throws Exception {
        int databaseSizeBeforeUpdate = bannersRepository.findAll().size();
        banners.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(banners)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banners in the database
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBanners() throws Exception {
        // Initialize the database
        bannersRepository.saveAndFlush(banners);

        int databaseSizeBeforeDelete = bannersRepository.findAll().size();

        // Delete the banners
        restBannersMockMvc
            .perform(delete(ENTITY_API_URL_ID, banners.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banners> bannersList = bannersRepository.findAll();
        assertThat(bannersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
