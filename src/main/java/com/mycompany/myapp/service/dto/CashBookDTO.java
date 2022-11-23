package com.mycompany.myapp.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CashBookDTO {

    private LocalDate date;
    private String description;
    private BigDecimal cr;
    private BigDecimal dr;

    public CashBookDTO() {}

    public CashBookDTO(LocalDate date, String description, BigDecimal cr, BigDecimal dr) {
        this.date = date;
        this.description = description;
        this.cr = cr;
        this.dr = dr;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
}
