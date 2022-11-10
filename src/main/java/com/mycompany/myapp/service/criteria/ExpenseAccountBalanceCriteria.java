package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ExpenseAccountBalance} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExpenseAccountBalanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expense-account-balances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseAccountBalanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter balance;

    private LongFilter expenseId;

    private LongFilter merchantId;

    private LongFilter transactionTypeId;

    private Boolean distinct;

    public ExpenseAccountBalanceCriteria() {}

    public ExpenseAccountBalanceCriteria(ExpenseAccountBalanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.expenseId = other.expenseId == null ? null : other.expenseId.copy();
        this.merchantId = other.merchantId == null ? null : other.merchantId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExpenseAccountBalanceCriteria copy() {
        return new ExpenseAccountBalanceCriteria(this);
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

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public BigDecimalFilter balance() {
        if (balance == null) {
            balance = new BigDecimalFilter();
        }
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public LongFilter getExpenseId() {
        return expenseId;
    }

    public LongFilter expenseId() {
        if (expenseId == null) {
            expenseId = new LongFilter();
        }
        return expenseId;
    }

    public void setExpenseId(LongFilter expenseId) {
        this.expenseId = expenseId;
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
        final ExpenseAccountBalanceCriteria that = (ExpenseAccountBalanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(expenseId, that.expenseId) &&
            Objects.equals(merchantId, that.merchantId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, expenseId, merchantId, transactionTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseAccountBalanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (balance != null ? "balance=" + balance + ", " : "") +
            (expenseId != null ? "expenseId=" + expenseId + ", " : "") +
            (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
            (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
