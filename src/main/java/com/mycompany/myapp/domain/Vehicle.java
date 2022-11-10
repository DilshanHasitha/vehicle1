package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "related_user_login")
    private String relatedUserLogin;

    @Column(name = "expence_code")
    private String expenceCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vehicles", "images", "exUsers", "employeeAccounts" }, allowSetters = true)
    private Merchant merchant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Vehicle code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Vehicle name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelatedUserLogin() {
        return this.relatedUserLogin;
    }

    public Vehicle relatedUserLogin(String relatedUserLogin) {
        this.setRelatedUserLogin(relatedUserLogin);
        return this;
    }

    public void setRelatedUserLogin(String relatedUserLogin) {
        this.relatedUserLogin = relatedUserLogin;
    }

    public String getExpenceCode() {
        return this.expenceCode;
    }

    public Vehicle expenceCode(String expenceCode) {
        this.setExpenceCode(expenceCode);
        return this;
    }

    public void setExpenceCode(String expenceCode) {
        this.expenceCode = expenceCode;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Vehicle isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Vehicle merchant(Merchant merchant) {
        this.setMerchant(merchant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return id != null && id.equals(((Vehicle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", relatedUserLogin='" + getRelatedUserLogin() + "'" +
            ", expenceCode='" + getExpenceCode() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
