package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExUser.
 */
@Entity
@Table(name = "ex_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "user_limit", precision = 21, scale = 2, nullable = false)
    private BigDecimal userLimit;

    @Column(name = "credit_score")
    private Double creditScore;

    @OneToOne
    @JoinColumn(unique = true)
    private User relatedUser;

    @ManyToMany(mappedBy = "exUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicles", "images", "exUsers", "employeeAccounts" }, allowSetters = true)
    private Set<Merchant> merchants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public ExUser login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ExUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ExUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public ExUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ExUser isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPhone() {
        return this.phone;
    }

    public ExUser phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public ExUser addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public ExUser addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public ExUser city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public ExUser country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ExUser image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ExUser imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public BigDecimal getUserLimit() {
        return this.userLimit;
    }

    public ExUser userLimit(BigDecimal userLimit) {
        this.setUserLimit(userLimit);
        return this;
    }

    public void setUserLimit(BigDecimal userLimit) {
        this.userLimit = userLimit;
    }

    public Double getCreditScore() {
        return this.creditScore;
    }

    public ExUser creditScore(Double creditScore) {
        this.setCreditScore(creditScore);
        return this;
    }

    public void setCreditScore(Double creditScore) {
        this.creditScore = creditScore;
    }

    public User getRelatedUser() {
        return this.relatedUser;
    }

    public void setRelatedUser(User user) {
        this.relatedUser = user;
    }

    public ExUser relatedUser(User user) {
        this.setRelatedUser(user);
        return this;
    }

    public Set<Merchant> getMerchants() {
        return this.merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        if (this.merchants != null) {
            this.merchants.forEach(i -> i.removeExUser(this));
        }
        if (merchants != null) {
            merchants.forEach(i -> i.addExUser(this));
        }
        this.merchants = merchants;
    }

    public ExUser merchants(Set<Merchant> merchants) {
        this.setMerchants(merchants);
        return this;
    }

    public ExUser addMerchant(Merchant merchant) {
        this.merchants.add(merchant);
        merchant.getExUsers().add(this);
        return this;
    }

    public ExUser removeMerchant(Merchant merchant) {
        this.merchants.remove(merchant);
        merchant.getExUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExUser)) {
            return false;
        }
        return id != null && id.equals(((ExUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExUser{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", userLimit=" + getUserLimit() +
            ", creditScore=" + getCreditScore() +
            "}";
    }
}
