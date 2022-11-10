package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Merchant} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MerchantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /merchants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MerchantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter merchantSecret;

    private StringFilter name;

    private BigDecimalFilter creditLimit;

    private BooleanFilter isActive;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private BigDecimalFilter percentage;

    private DoubleFilter creditScore;

    private StringFilter email;

    private DoubleFilter rating;

    private IntegerFilter leadTime;

    private BooleanFilter isSandBox;

    private StringFilter storeDescription;

    private StringFilter storeSecondaryDescription;

    private LongFilter vehicleId;

    private LongFilter imagesId;

    private LongFilter exUserId;

    private LongFilter employeeAccountId;

    private Boolean distinct;

    public MerchantCriteria() {}

    public MerchantCriteria(MerchantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.merchantSecret = other.merchantSecret == null ? null : other.merchantSecret.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.creditLimit = other.creditLimit == null ? null : other.creditLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.percentage = other.percentage == null ? null : other.percentage.copy();
        this.creditScore = other.creditScore == null ? null : other.creditScore.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.leadTime = other.leadTime == null ? null : other.leadTime.copy();
        this.isSandBox = other.isSandBox == null ? null : other.isSandBox.copy();
        this.storeDescription = other.storeDescription == null ? null : other.storeDescription.copy();
        this.storeSecondaryDescription = other.storeSecondaryDescription == null ? null : other.storeSecondaryDescription.copy();
        this.vehicleId = other.vehicleId == null ? null : other.vehicleId.copy();
        this.imagesId = other.imagesId == null ? null : other.imagesId.copy();
        this.exUserId = other.exUserId == null ? null : other.exUserId.copy();
        this.employeeAccountId = other.employeeAccountId == null ? null : other.employeeAccountId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MerchantCriteria copy() {
        return new MerchantCriteria(this);
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

    public StringFilter getMerchantSecret() {
        return merchantSecret;
    }

    public StringFilter merchantSecret() {
        if (merchantSecret == null) {
            merchantSecret = new StringFilter();
        }
        return merchantSecret;
    }

    public void setMerchantSecret(StringFilter merchantSecret) {
        this.merchantSecret = merchantSecret;
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

    public BigDecimalFilter getCreditLimit() {
        return creditLimit;
    }

    public BigDecimalFilter creditLimit() {
        if (creditLimit == null) {
            creditLimit = new BigDecimalFilter();
        }
        return creditLimit;
    }

    public void setCreditLimit(BigDecimalFilter creditLimit) {
        this.creditLimit = creditLimit;
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

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public StringFilter addressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new StringFilter();
        }
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public StringFilter addressLine2() {
        if (addressLine2 == null) {
            addressLine2 = new StringFilter();
        }
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public BigDecimalFilter getPercentage() {
        return percentage;
    }

    public BigDecimalFilter percentage() {
        if (percentage == null) {
            percentage = new BigDecimalFilter();
        }
        return percentage;
    }

    public void setPercentage(BigDecimalFilter percentage) {
        this.percentage = percentage;
    }

    public DoubleFilter getCreditScore() {
        return creditScore;
    }

    public DoubleFilter creditScore() {
        if (creditScore == null) {
            creditScore = new DoubleFilter();
        }
        return creditScore;
    }

    public void setCreditScore(DoubleFilter creditScore) {
        this.creditScore = creditScore;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public DoubleFilter rating() {
        if (rating == null) {
            rating = new DoubleFilter();
        }
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public IntegerFilter getLeadTime() {
        return leadTime;
    }

    public IntegerFilter leadTime() {
        if (leadTime == null) {
            leadTime = new IntegerFilter();
        }
        return leadTime;
    }

    public void setLeadTime(IntegerFilter leadTime) {
        this.leadTime = leadTime;
    }

    public BooleanFilter getIsSandBox() {
        return isSandBox;
    }

    public BooleanFilter isSandBox() {
        if (isSandBox == null) {
            isSandBox = new BooleanFilter();
        }
        return isSandBox;
    }

    public void setIsSandBox(BooleanFilter isSandBox) {
        this.isSandBox = isSandBox;
    }

    public StringFilter getStoreDescription() {
        return storeDescription;
    }

    public StringFilter storeDescription() {
        if (storeDescription == null) {
            storeDescription = new StringFilter();
        }
        return storeDescription;
    }

    public void setStoreDescription(StringFilter storeDescription) {
        this.storeDescription = storeDescription;
    }

    public StringFilter getStoreSecondaryDescription() {
        return storeSecondaryDescription;
    }

    public StringFilter storeSecondaryDescription() {
        if (storeSecondaryDescription == null) {
            storeSecondaryDescription = new StringFilter();
        }
        return storeSecondaryDescription;
    }

    public void setStoreSecondaryDescription(StringFilter storeSecondaryDescription) {
        this.storeSecondaryDescription = storeSecondaryDescription;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            vehicleId = new LongFilter();
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
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

    public LongFilter getExUserId() {
        return exUserId;
    }

    public LongFilter exUserId() {
        if (exUserId == null) {
            exUserId = new LongFilter();
        }
        return exUserId;
    }

    public void setExUserId(LongFilter exUserId) {
        this.exUserId = exUserId;
    }

    public LongFilter getEmployeeAccountId() {
        return employeeAccountId;
    }

    public LongFilter employeeAccountId() {
        if (employeeAccountId == null) {
            employeeAccountId = new LongFilter();
        }
        return employeeAccountId;
    }

    public void setEmployeeAccountId(LongFilter employeeAccountId) {
        this.employeeAccountId = employeeAccountId;
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
        final MerchantCriteria that = (MerchantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(merchantSecret, that.merchantSecret) &&
            Objects.equals(name, that.name) &&
            Objects.equals(creditLimit, that.creditLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(percentage, that.percentage) &&
            Objects.equals(creditScore, that.creditScore) &&
            Objects.equals(email, that.email) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(leadTime, that.leadTime) &&
            Objects.equals(isSandBox, that.isSandBox) &&
            Objects.equals(storeDescription, that.storeDescription) &&
            Objects.equals(storeSecondaryDescription, that.storeSecondaryDescription) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(exUserId, that.exUserId) &&
            Objects.equals(employeeAccountId, that.employeeAccountId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            merchantSecret,
            name,
            creditLimit,
            isActive,
            phone,
            addressLine1,
            addressLine2,
            city,
            country,
            percentage,
            creditScore,
            email,
            rating,
            leadTime,
            isSandBox,
            storeDescription,
            storeSecondaryDescription,
            vehicleId,
            imagesId,
            exUserId,
            employeeAccountId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MerchantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (merchantSecret != null ? "merchantSecret=" + merchantSecret + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (creditLimit != null ? "creditLimit=" + creditLimit + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (percentage != null ? "percentage=" + percentage + ", " : "") +
            (creditScore != null ? "creditScore=" + creditScore + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (leadTime != null ? "leadTime=" + leadTime + ", " : "") +
            (isSandBox != null ? "isSandBox=" + isSandBox + ", " : "") +
            (storeDescription != null ? "storeDescription=" + storeDescription + ", " : "") +
            (storeSecondaryDescription != null ? "storeSecondaryDescription=" + storeSecondaryDescription + ", " : "") +
            (vehicleId != null ? "vehicleId=" + vehicleId + ", " : "") +
            (imagesId != null ? "imagesId=" + imagesId + ", " : "") +
            (exUserId != null ? "exUserId=" + exUserId + ", " : "") +
            (employeeAccountId != null ? "employeeAccountId=" + employeeAccountId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
