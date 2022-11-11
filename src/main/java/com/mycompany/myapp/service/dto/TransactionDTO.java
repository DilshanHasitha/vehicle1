package com.mycompany.myapp.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {

    private String vehicleNo;
    private LocalDate date;
    private BigDecimal cr;
    private BigDecimal dr;

    public TransactionDTO() {}

    public TransactionDTO(String vehicleNo, LocalDate date, BigDecimal cr, BigDecimal dr) {
        this.vehicleNo = vehicleNo;
        this.date = date;
        this.cr = cr;
        this.dr = dr;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
