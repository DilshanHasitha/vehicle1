package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Vehicle} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter relatedUserLogin;

    private StringFilter expenceCode;

    private BooleanFilter isActive;

    private LongFilter merchantId;

    private Boolean distinct;

    public VehicleCriteria() {}

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.relatedUserLogin = other.relatedUserLogin == null ? null : other.relatedUserLogin.copy();
        this.expenceCode = other.expenceCode == null ? null : other.expenceCode.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.merchantId = other.merchantId == null ? null : other.merchantId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getRelatedUserLogin() {
        return relatedUserLogin;
    }

    public StringFilter relatedUserLogin() {
        if (relatedUserLogin == null) {
            relatedUserLogin = new StringFilter();
        }
        return relatedUserLogin;
    }

    public void setRelatedUserLogin(StringFilter relatedUserLogin) {
        this.relatedUserLogin = relatedUserLogin;
    }

    public StringFilter getExpenceCode() {
        return expenceCode;
    }

    public StringFilter expenceCode() {
        if (expenceCode == null) {
            expenceCode = new StringFilter();
        }
        return expenceCode;
    }

    public void setExpenceCode(StringFilter expenceCode) {
        this.expenceCode = expenceCode;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getMerchantId() {
        return merchantId;
    }

    public LongFilter merchantId() {
        if (merchantId == null) {
            merchantId = new LongFilter();
        }
        return merchantId;
    }

    public void setMerchantId(LongFilter merchantId) {
        this.merchantId = merchantId;
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
        final VehicleCriteria that = (VehicleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(relatedUserLogin, that.relatedUserLogin) &&
            Objects.equals(expenceCode, that.expenceCode) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(merchantId, that.merchantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, relatedUserLogin, expenceCode, isActive, merchantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (relatedUserLogin != null ? "relatedUserLogin=" + relatedUserLogin + ", " : "") +
            (expenceCode != null ? "expenceCode=" + expenceCode + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
