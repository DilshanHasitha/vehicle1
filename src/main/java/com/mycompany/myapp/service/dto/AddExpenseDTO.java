package com.mycompany.myapp.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AddExpenseDTO {

    private String description;
    private BigDecimal cr;
    private BigDecimal dr;

    private BigDecimal amount;

    private String expenseCode;

    private String merchantCode;

    public AddExpenseDTO() {}

    public AddExpenseDTO(String description, BigDecimal cr, BigDecimal dr, BigDecimal amount, String expenseCode, String merchantCode) {
        this.description = description;
        this.cr = cr;
        this.dr = dr;
        this.amount = amount;
        this.expenseCode = expenseCode;
        this.merchantCode = merchantCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCr() {
        return cr;
    }

    public void setCr(BigDecimal cr) {
        this.cr = cr;
    }

    public BigDecimal getDr() {
        return dr;
    }

    public void setDr(BigDecimal dr) {
        this.dr = dr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExpenseCode() {
        return expenseCode;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }
}
