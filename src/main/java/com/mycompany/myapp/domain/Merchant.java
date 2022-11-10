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
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "merchant_secret", nullable = false)
    private String merchantSecret;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "credit_limit", precision = 21, scale = 2, nullable = false)
    private BigDecimal creditLimit;

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

    @NotNull
    @Column(name = "percentage", precision = 21, scale = 2, nullable = false)
    private BigDecimal percentage;

    @Column(name = "credit_score")
    private Double creditScore;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "is_sand_box")
    private Boolean isSandBox;

    @Column(name = "store_description")
    private String storeDescription;

    @Column(name = "store_secondary_description")
    private String storeSecondaryDescription;

    @OneToMany(mappedBy = "merchant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "merchant" }, allowSetters = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee", "merchants", "banners" }, allowSetters = true)
    private Images images;

    @ManyToMany
    @JoinTable(
        name = "rel_merchant__ex_user",
        joinColumns = @JoinColumn(name = "merchant_id"),
        inverseJoinColumns = @JoinColumn(name = "ex_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatedUser", "merchants" }, allowSetters = true)
    private Set<ExUser> exUsers = new HashSet<>();

    @OneToMany(mappedBy = "merchant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transactionType", "merchant", "employee" }, allowSetters = true)
    private Set<EmployeeAccount> employeeAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Merchant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Merchant code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMerchantSecret() {
        return this.merchantSecret;
    }

    public Merchant merchantSecret(String merchantSecret) {
        this.setMerchantSecret(merchantSecret);
        return this;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }

    public String getName() {
        return this.name;
    }

    public Merchant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public Merchant creditLimit(BigDecimal creditLimit) {
        this.setCreditLimit(creditLimit);
        return this;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Merchant isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPhone() {
        return this.phone;
    }

    public Merchant phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Merchant addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public Merchant addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public Merchant city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Merchant country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getPercentage() {
        return this.percentage;
    }

    public Merchant percentage(BigDecimal percentage) {
        this.setPercentage(percentage);
        return this;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Double getCreditScore() {
        return this.creditScore;
    }

    public Merchant creditScore(Double creditScore) {
        this.setCreditScore(creditScore);
        return this;
    }

    public void setCreditScore(Double creditScore) {
        this.creditScore = creditScore;
    }

    public String getEmail() {
        return this.email;
    }

    public Merchant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRating() {
        return this.rating;
    }

    public Merchant rating(Double rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getLeadTime() {
        return this.leadTime;
    }

    public Merchant leadTime(Integer leadTime) {
        this.setLeadTime(leadTime);
        return this;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Boolean getIsSandBox() {
        return this.isSandBox;
    }

    public Merchant isSandBox(Boolean isSandBox) {
        this.setIsSandBox(isSandBox);
        return this;
    }

    public void setIsSandBox(Boolean isSandBox) {
        this.isSandBox = isSandBox;
    }

    public String getStoreDescription() {
        return this.storeDescription;
    }

    public Merchant storeDescription(String storeDescription) {
        this.setStoreDescription(storeDescription);
        return this;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreSecondaryDescription() {
        return this.storeSecondaryDescription;
    }

    public Merchant storeSecondaryDescription(String storeSecondaryDescription) {
        this.setStoreSecondaryDescription(storeSecondaryDescription);
        return this;
    }

    public void setStoreSecondaryDescription(String storeSecondaryDescription) {
        this.storeSecondaryDescription = storeSecondaryDescription;
    }

    public Set<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        if (this.vehicles != null) {
            this.vehicles.forEach(i -> i.setMerchant(null));
        }
        if (vehicles != null) {
            vehicles.forEach(i -> i.setMerchant(this));
        }
        this.vehicles = vehicles;
    }

    public Merchant vehicles(Set<Vehicle> vehicles) {
        this.setVehicles(vehicles);
        return this;
    }

    public Merchant addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.setMerchant(this);
        return this;
    }

    public Merchant removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.setMerchant(null);
        return this;
    }

    public Images getImages() {
        return this.images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Merchant images(Images images) {
        this.setImages(images);
        return this;
    }

    public Set<ExUser> getExUsers() {
        return this.exUsers;
    }

    public void setExUsers(Set<ExUser> exUsers) {
        this.exUsers = exUsers;
    }

    public Merchant exUsers(Set<ExUser> exUsers) {
        this.setExUsers(exUsers);
        return this;
    }

    public Merchant addExUser(ExUser exUser) {
        this.exUsers.add(exUser);
        exUser.getMerchants().add(this);
        return this;
    }

    public Merchant removeExUser(ExUser exUser) {
        this.exUsers.remove(exUser);
        exUser.getMerchants().remove(this);
        return this;
    }

    public Set<EmployeeAccount> getEmployeeAccounts() {
        return this.employeeAccounts;
    }

    public void setEmployeeAccounts(Set<EmployeeAccount> employeeAccounts) {
        if (this.employeeAccounts != null) {
            this.employeeAccounts.forEach(i -> i.setMerchant(null));
        }
        if (employeeAccounts != null) {
            employeeAccounts.forEach(i -> i.setMerchant(this));
        }
        this.employeeAccounts = employeeAccounts;
    }

    public Merchant employeeAccounts(Set<EmployeeAccount> employeeAccounts) {
        this.setEmployeeAccounts(employeeAccounts);
        return this;
    }

    public Merchant addEmployeeAccount(EmployeeAccount employeeAccount) {
        this.employeeAccounts.add(employeeAccount);
        employeeAccount.setMerchant(this);
        return this;
    }

    public Merchant removeEmployeeAccount(EmployeeAccount employeeAccount) {
        this.employeeAccounts.remove(employeeAccount);
        employeeAccount.setMerchant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Merchant)) {
            return false;
        }
        return id != null && id.equals(((Merchant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", merchantSecret='" + getMerchantSecret() + "'" +
            ", name='" + getName() + "'" +
            ", creditLimit=" + getCreditLimit() +
            ", isActive='" + getIsActive() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", percentage=" + getPercentage() +
            ", creditScore=" + getCreditScore() +
            ", email='" + getEmail() + "'" +
            ", rating=" + getRating() +
            ", leadTime=" + getLeadTime() +
            ", isSandBox='" + getIsSandBox() + "'" +
            ", storeDescription='" + getStoreDescription() + "'" +
            ", storeSecondaryDescription='" + getStoreSecondaryDescription() + "'" +
            "}";
    }
}
