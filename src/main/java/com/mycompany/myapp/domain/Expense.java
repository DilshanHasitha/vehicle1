package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "expense_code", nullable = false)
    private String expenseCode;

    @NotNull
    @Column(name = "expense_name", nullable = false)
    private String expenseName;

    @Column(name = "expense_limit", precision = 21, scale = 2)
    private BigDecimal expenseLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseCode() {
        return this.expenseCode;
    }

    public Expense expenseCode(String expenseCode) {
        this.setExpenseCode(expenseCode);
        return this;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public Expense expenseName(String expenseName) {
        this.setExpenseName(expenseName);
        return this;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public BigDecimal getExpenseLimit() {
        return this.expenseLimit;
    }

    public Expense expenseLimit(BigDecimal expenseLimit) {
        this.setExpenseLimit(expenseLimit);
        return this;
    }

    public void setExpenseLimit(BigDecimal expenseLimit) {
        this.expenseLimit = expenseLimit;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Expense isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", expenseCode='" + getExpenseCode() + "'" +
            ", expenseName='" + getExpenseName() + "'" +
            ", expenseLimit=" + getExpenseLimit() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
