package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Expense} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExpenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter expenseCode;

    private StringFilter expenseName;

    private BigDecimalFilter expenseLimit;

    private BooleanFilter isActive;

    private Boolean distinct;

    public ExpenseCriteria() {}

    public ExpenseCriteria(ExpenseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.expenseCode = other.expenseCode == null ? null : other.expenseCode.copy();
        this.expenseName = other.expenseName == null ? null : other.expenseName.copy();
        this.expenseLimit = other.expenseLimit == null ? null : other.expenseLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExpenseCriteria copy() {
        return new ExpenseCriteria(this);
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

    public StringFilter getExpenseCode() {
        return expenseCode;
    }

    public StringFilter expenseCode() {
        if (expenseCode == null) {
            expenseCode = new StringFilter();
        }
        return expenseCode;
    }

    public void setExpenseCode(StringFilter expenseCode) {
        this.expenseCode = expenseCode;
    }

    public StringFilter getExpenseName() {
        return expenseName;
    }

    public StringFilter expenseName() {
        if (expenseName == null) {
            expenseName = new StringFilter();
        }
        return expenseName;
    }

    public void setExpenseName(StringFilter expenseName) {
        this.expenseName = expenseName;
    }

    public BigDecimalFilter getExpenseLimit() {
        return expenseLimit;
    }

    public BigDecimalFilter expenseLimit() {
        if (expenseLimit == null) {
            expenseLimit = new BigDecimalFilter();
        }
        return expenseLimit;
    }

    public void setExpenseLimit(BigDecimalFilter expenseLimit) {
        this.expenseLimit = expenseLimit;
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
        final ExpenseCriteria that = (ExpenseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(expenseCode, that.expenseCode) &&
            Objects.equals(expenseName, that.expenseName) &&
            Objects.equals(expenseLimit, that.expenseLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expenseCode, expenseName, expenseLimit, isActive, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (expenseCode != null ? "expenseCode=" + expenseCode + ", " : "") +
            (expenseName != null ? "expenseName=" + expenseName + ", " : "") +
            (expenseLimit != null ? "expenseLimit=" + expenseLimit + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
