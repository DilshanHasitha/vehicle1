package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public class RequestTransDTO {

    private String merchantCode;
    private LocalDate date;

    public RequestTransDTO(String merchantCode, LocalDate date) {
        this.merchantCode = merchantCode;
        this.date = date;
    }

    public RequestTransDTO() {}

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
