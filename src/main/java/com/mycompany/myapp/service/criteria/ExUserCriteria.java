package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ExUser} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ex-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private BooleanFilter isActive;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private BigDecimalFilter userLimit;

    private DoubleFilter creditScore;

    private LongFilter relatedUserId;

    private LongFilter merchantId;

    private Boolean distinct;

    public ExUserCriteria() {}

    public ExUserCriteria(ExUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.userLimit = other.userLimit == null ? null : other.userLimit.copy();
        this.creditScore = other.creditScore == null ? null : other.creditScore.copy();
        this.relatedUserId = other.relatedUserId == null ? null : other.relatedUserId.copy();
        this.merchantId = other.merchantId == null ? null : other.merchantId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExUserCriteria copy() {
        return new ExUserCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
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

    public BigDecimalFilter getUserLimit() {
        return userLimit;
    }

    public BigDecimalFilter userLimit() {
        if (userLimit == null) {
            userLimit = new BigDecimalFilter();
        }
        return userLimit;
    }

    public void setUserLimit(BigDecimalFilter userLimit) {
        this.userLimit = userLimit;
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

    public LongFilter getRelatedUserId() {
        return relatedUserId;
    }

    public LongFilter relatedUserId() {
        if (relatedUserId == null) {
            relatedUserId = new LongFilter();
        }
        return relatedUserId;
    }

    public void setRelatedUserId(LongFilter relatedUserId) {
        this.relatedUserId = relatedUserId;
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
        final ExUserCriteria that = (ExUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(userLimit, that.userLimit) &&
            Objects.equals(creditScore, that.creditScore) &&
            Objects.equals(relatedUserId, that.relatedUserId) &&
            Objects.equals(merchantId, that.merchantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            login,
            firstName,
            lastName,
            email,
            isActive,
            phone,
            addressLine1,
            addressLine2,
            city,
            country,
            userLimit,
            creditScore,
            relatedUserId,
            merchantId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (login != null ? "login=" + login + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (userLimit != null ? "userLimit=" + userLimit + ", " : "") +
            (creditScore != null ? "creditScore=" + creditScore + ", " : "") +
            (relatedUserId != null ? "relatedUserId=" + relatedUserId + ", " : "") +
            (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
