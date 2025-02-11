package com.m2iAssurance.model;

import com.m2iAssurance.enums.PropertyType;
import lombok.Data;


@Data
public class PropertyInsuranceDetails {
    private String propertyAddress;
    private Double propertySize;
    private Integer propertyAge;
    private Boolean hasSecuritySystem;
    private PropertyType propertyType;
}
