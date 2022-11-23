package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeAccount.
 */
@Entity
@Table(name = "employee_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @NotNull
    @Column(name = "transaction_amount_dr", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmountDR;

    @NotNull
    @Column(name = "transaction_amount_cr", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmountCR;

    @NotNull
    @Column(name = "transaction_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionBalance;

    @ManyToOne
    private TransactionType transactionType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vehicles", "images", "exUsers", "employeeAccounts" }, allowSetters = true)
    private Merchant merchant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "images", "type", "vehicles", "merchant" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public EmployeeAccount transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return this.transactionDescription;
    }

    public EmployeeAccount transactionDescription(String transactionDescription) {
        this.setTransactionDescription(transactionDescription);
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public BigDecimal getTransactionAmountDR() {
        return this.transactionAmountDR;
    }

    public EmployeeAccount transactionAmountDR(BigDecimal transactionAmountDR) {
        this.setTransactionAmountDR(transactionAmountDR);
        return this;
    }

    public void setTransactionAmountDR(BigDecimal transactionAmountDR) {
        this.transactionAmountDR = transactionAmountDR;
    }

    public BigDecimal getTransactionAmountCR() {
        return this.transactionAmountCR;
    }

    public EmployeeAccount transactionAmountCR(BigDecimal transactionAmountCR) {
        this.setTransactionAmountCR(transactionAmountCR);
        return this;
    }

    public void setTransactionAmountCR(BigDecimal transactionAmountCR) {
        this.transactionAmountCR = transactionAmountCR;
    }

    public BigDecimal getTransactionBalance() {
        return this.transactionBalance;
    }

    public EmployeeAccount transactionBalance(BigDecimal transactionBalance) {
        this.setTransactionBalance(transactionBalance);
        return this;
    }

    public void setTransactionBalance(BigDecimal transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public EmployeeAccount transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public EmployeeAccount merchant(Merchant merchant) {
        this.setMerchant(merchant);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeAccount employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeAccount)) {
            return false;
        }
        return id != null && id.equals(((EmployeeAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeAccount{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionAmountDR=" + getTransactionAmountDR() +
            ", transactionAmountCR=" + getTransactionAmountCR() +
            ", transactionBalance=" + getTransactionBalance() +
            "}";
    }
}
