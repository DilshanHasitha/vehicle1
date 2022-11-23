package com.mycompany.myapp.service.dto;

public class EmployeeDTO {

    private String name;
    private String vehicleNo;
    private String phone;

    public EmployeeDTO() {}

    public EmployeeDTO(String name, String vehicleNo, String phone) {
        this.name = name;
        this.vehicleNo = vehicleNo;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
