package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeAccountBalance.
 */
@Entity
@Table(name = "employee_account_balance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeAccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "images", "type" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vehicles", "images", "exUsers", "employeeAccounts" }, allowSetters = true)
    private Merchant merchant;

    @ManyToOne
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeAccountBalance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public EmployeeAccountBalance balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeAccountBalance employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public EmployeeAccountBalance merchant(Merchant merchant) {
        this.setMerchant(merchant);
        return this;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public EmployeeAccountBalance transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeAccountBalance)) {
            return false;
        }
        return id != null && id.equals(((EmployeeAccountBalance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeAccountBalance{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            "}";
    }
}
