package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public class TrnByExpenseCodeDTO {

    private String merchantCode;
    private String expenseCode;

    public TrnByExpenseCodeDTO(String merchantCode, String expenseCode) {
        this.merchantCode = merchantCode;
        this.expenseCode = expenseCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getExpenseCode() {
        return expenseCode;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }
}
