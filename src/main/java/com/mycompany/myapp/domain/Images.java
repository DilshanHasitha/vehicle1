package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Images.
 */
@Entity
@Table(name = "images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imglob_content_type")
    private String imglobContentType;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "low_res_url")
    private String lowResURL;

    @Column(name = "original_url")
    private String originalURL;

    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "image_blob_content_type")
    private String imageBlobContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "images", "type", "vehicles", "merchant" }, allowSetters = true)
    private Employee employee;

    @OneToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicles", "images", "exUsers", "employeeAccounts" }, allowSetters = true)
    private Set<Merchant> merchants = new HashSet<>();

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images" }, allowSetters = true)
    private Set<Banners> banners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Images id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImglobContentType() {
        return this.imglobContentType;
    }

    public Images imglobContentType(String imglobContentType) {
        this.setImglobContentType(imglobContentType);
        return this;
    }

    public void setImglobContentType(String imglobContentType) {
        this.imglobContentType = imglobContentType;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Images imageURL(String imageURL) {
        this.setImageURL(imageURL);
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return this.imageName;
    }

    public Images imageName(String imageName) {
        this.setImageName(imageName);
        return this;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLowResURL() {
        return this.lowResURL;
    }

    public Images lowResURL(String lowResURL) {
        this.setLowResURL(lowResURL);
        return this;
    }

    public void setLowResURL(String lowResURL) {
        this.lowResURL = lowResURL;
    }

    public String getOriginalURL() {
        return this.originalURL;
    }

    public Images originalURL(String originalURL) {
        this.setOriginalURL(originalURL);
        return this;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public byte[] getImageBlob() {
        return this.imageBlob;
    }

    public Images imageBlob(byte[] imageBlob) {
        this.setImageBlob(imageBlob);
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return this.imageBlobContentType;
    }

    public Images imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Images employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Set<Merchant> getMerchants() {
        return this.merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        if (this.merchants != null) {
            this.merchants.forEach(i -> i.setImages(null));
        }
        if (merchants != null) {
            merchants.forEach(i -> i.setImages(this));
        }
        this.merchants = merchants;
    }

    public Images merchants(Set<Merchant> merchants) {
        this.setMerchants(merchants);
        return this;
    }

    public Images addMerchant(Merchant merchant) {
        this.merchants.add(merchant);
        merchant.setImages(this);
        return this;
    }

    public Images removeMerchant(Merchant merchant) {
        this.merchants.remove(merchant);
        merchant.setImages(null);
        return this;
    }

    public Set<Banners> getBanners() {
        return this.banners;
    }

    public void setBanners(Set<Banners> banners) {
        if (this.banners != null) {
            this.banners.forEach(i -> i.removeImages(this));
        }
        if (banners != null) {
            banners.forEach(i -> i.addImages(this));
        }
        this.banners = banners;
    }

    public Images banners(Set<Banners> banners) {
        this.setBanners(banners);
        return this;
    }

    public Images addBanners(Banners banners) {
        this.banners.add(banners);
        banners.getImages().add(this);
        return this;
    }

    public Images removeBanners(Banners banners) {
        this.banners.remove(banners);
        banners.getImages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Images)) {
            return false;
        }
        return id != null && id.equals(((Images) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Images{" +
            "id=" + getId() +
            ", imglobContentType='" + getImglobContentType() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", imageName='" + getImageName() + "'" +
            ", lowResURL='" + getLowResURL() + "'" +
            ", originalURL='" + getOriginalURL() + "'" +
            ", imageBlob='" + getImageBlob() + "'" +
            ", imageBlobContentType='" + getImageBlobContentType() + "'" +
            "}";
    }
}
