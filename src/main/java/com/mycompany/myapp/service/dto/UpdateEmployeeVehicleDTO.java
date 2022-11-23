package com.mycompany.myapp.service.dto;

public class UpdateEmployeeVehicleDTO {

    private String phone;
    private String vehicle;

    public UpdateEmployeeVehicleDTO(String phone, String vehicle) {
        this.phone = phone;
        this.vehicle = vehicle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
