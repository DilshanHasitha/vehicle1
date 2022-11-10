package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.EmployeeAccount} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EmployeeAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employee-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter transactionDate;

    private StringFilter transactionDescription;

    private BigDecimalFilter transactionAmountDR;

    private BigDecimalFilter transactionAmountCR;

    private BigDecimalFilter transactionBalance;

    private LongFilter transactionTypeId;

    private LongFilter merchantId;

    private LongFilter employeeId;

    private Boolean distinct;

    public EmployeeAccountCriteria() {}

    public EmployeeAccountCriteria(EmployeeAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionDescription = other.transactionDescription == null ? null : other.transactionDescription.copy();
        this.transactionAmountDR = other.transactionAmountDR == null ? null : other.transactionAmountDR.copy();
        this.transactionAmountCR = other.transactionAmountCR == null ? null : other.transactionAmountCR.copy();
        this.transactionBalance = other.transactionBalance == null ? null : other.transactionBalance.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.merchantId = other.merchantId == null ? null : other.merchantId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeAccountCriteria copy() {
        return new EmployeeAccountCriteria(this);
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

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public StringFilter getTransactionDescription() {
        return transactionDescription;
    }

    public StringFilter transactionDescription() {
        if (transactionDescription == null) {
            transactionDescription = new StringFilter();
        }
        return transactionDescription;
    }

    public void setTransactionDescription(StringFilter transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public BigDecimalFilter getTransactionAmountDR() {
        return transactionAmountDR;
    }

    public BigDecimalFilter transactionAmountDR() {
        if (transactionAmountDR == null) {
            transactionAmountDR = new BigDecimalFilter();
        }
        return transactionAmountDR;
    }

    public void setTransactionAmountDR(BigDecimalFilter transactionAmountDR) {
        this.transactionAmountDR = transactionAmountDR;
    }

    public BigDecimalFilter getTransactionAmountCR() {
        return transactionAmountCR;
    }

    public BigDecimalFilter transactionAmountCR() {
        if (transactionAmountCR == null) {
            transactionAmountCR = new BigDecimalFilter();
        }
        return transactionAmountCR;
    }

    public void setTransactionAmountCR(BigDecimalFilter transactionAmountCR) {
        this.transactionAmountCR = transactionAmountCR;
    }

    public BigDecimalFilter getTransactionBalance() {
        return transactionBalance;
    }

    public BigDecimalFilter transactionBalance() {
        if (transactionBalance == null) {
            transactionBalance = new BigDecimalFilter();
        }
        return transactionBalance;
    }

    public void setTransactionBalance(BigDecimalFilter transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public LongFilter transactionTypeId() {
        if (transactionTypeId == null) {
            transactionTypeId = new LongFilter();
        }
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
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

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
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
        final EmployeeAccountCriteria that = (EmployeeAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionDescription, that.transactionDescription) &&
            Objects.equals(transactionAmountDR, that.transactionAmountDR) &&
            Objects.equals(transactionAmountCR, that.transactionAmountCR) &&
            Objects.equals(transactionBalance, that.transactionBalance) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(merchantId, that.merchantId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transactionDate,
            transactionDescription,
            transactionAmountDR,
            transactionAmountCR,
            transactionBalance,
            transactionTypeId,
            merchantId,
            employeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (transactionDescription != null ? "transactionDescription=" + transactionDescription + ", " : "") +
            (transactionAmountDR != null ? "transactionAmountDR=" + transactionAmountDR + ", " : "") +
            (transactionAmountCR != null ? "transactionAmountCR=" + transactionAmountCR + ", " : "") +
            (transactionBalance != null ? "transactionBalance=" + transactionBalance + ", " : "") +
            (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
