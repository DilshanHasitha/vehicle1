package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Banners} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BannersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /banners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BannersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter heading;

    private StringFilter description;

    private StringFilter link;

    private LongFilter imagesId;

    private Boolean distinct;

    public BannersCriteria() {}

    public BannersCriteria(BannersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.heading = other.heading == null ? null : other.heading.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.imagesId = other.imagesId == null ? null : other.imagesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BannersCriteria copy() {
        return new BannersCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getHeading() {
        return heading;
    }

    public StringFilter heading() {
        if (heading == null) {
            heading = new StringFilter();
        }
        return heading;
    }

    public void setHeading(StringFilter heading) {
        this.heading = heading;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLink() {
        return link;
    }

    public StringFilter link() {
        if (link == null) {
            link = new StringFilter();
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            imagesId = new LongFilter();
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BannersCriteria that = (BannersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(heading, that.heading) &&
            Objects.equals(description, that.description) &&
            Objects.equals(link, that.link) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, heading, description, link, imagesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BannersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (heading != null ? "heading=" + heading + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (imagesId != null ? "imagesId=" + imagesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
