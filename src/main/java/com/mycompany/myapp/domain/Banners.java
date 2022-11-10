package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Banners.
 */
@Entity
@Table(name = "banners")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Banners implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "heading")
    private String heading;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @ManyToMany
    @JoinTable(
        name = "rel_banners__images",
        joinColumns = @JoinColumn(name = "banners_id"),
        inverseJoinColumns = @JoinColumn(name = "images_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "merchants", "banners" }, allowSetters = true)
    private Set<Images> images = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Banners id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Banners code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHeading() {
        return this.heading;
    }

    public Banners heading(String heading) {
        this.setHeading(heading);
        return this;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return this.description;
    }

    public Banners description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public Banners link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Images> getImages() {
        return this.images;
    }

    public void setImages(Set<Images> images) {
        this.images = images;
    }

    public Banners images(Set<Images> images) {
        this.setImages(images);
        return this;
    }

    public Banners addImages(Images images) {
        this.images.add(images);
        images.getBanners().add(this);
        return this;
    }

    public Banners removeImages(Images images) {
        this.images.remove(images);
        images.getBanners().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banners)) {
            return false;
        }
        return id != null && id.equals(((Banners) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banners{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", heading='" + getHeading() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
