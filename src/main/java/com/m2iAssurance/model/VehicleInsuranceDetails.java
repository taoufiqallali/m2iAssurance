package com.m2iAssurance.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VehicleInsuranceDetails {
    private String vehicleMake;
    private String vehicleModel;
    private LocalDate vehicleYear;
    private String vehicleVin;
    private String driverLicense;
    private Boolean isCommercialUse;
    private Integer annualMileage;
}
