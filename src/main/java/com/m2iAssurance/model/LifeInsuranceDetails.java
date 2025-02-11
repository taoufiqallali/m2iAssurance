package com.m2iAssurance.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LifeInsuranceDetails {
    private String beneficiaryName;
    private String beneficiaryRelationship;
    private String medicalHistory;
    private List<String> preExistingConditions;
    private String occupation;
    private BigDecimal annualIncome;

}
